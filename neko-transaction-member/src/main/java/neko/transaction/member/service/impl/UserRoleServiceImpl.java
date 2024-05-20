package neko.transaction.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.RoleSortType;
import neko.transaction.member.entity.UserRole;
import neko.transaction.member.entity.WeightRoleRelation;
import neko.transaction.member.mapper.UserRoleMapper;
import neko.transaction.member.mapper.WeightRoleRelationMapper;
import neko.transaction.member.service.UserRoleService;
import neko.transaction.member.vo.NewUserRoleVo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Resource
    private WeightRoleRelationMapper weightRoleRelationMapper;

    /**
     * 添加角色
     * @param vo 添加角色vo
     */
    @Override
    public void newUserRole(NewUserRoleVo vo) {
        if(this.baseMapper.selectOne(new QueryWrapper<UserRole>().eq("role_type", vo.getRoleType())) != null){
            throw new DuplicateKeyException("roleType重复");
        }

        UserRole userRole = new UserRole();
        LocalDateTime now = LocalDateTime.now();
        BeanUtil.copyProperties(vo, userRole);
        userRole.setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(userRole);
    }

    /**
     * 分页查询角色信息
     * @param vo 分页查询vo
     * @return 分页查询结果
     */
    @Override
    public Page<UserRole> getUserRolesByQueryLimitedPage(QueryVo vo) {
        Page<UserRole> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(UserRole::getRoleType, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 获取管理员角色
     * @return 管理员角色
     */
    @Override
    public List<UserRole> getAdminRoles() {
        return this.lambdaQuery().eq(UserRole::getType, RoleSortType.ADMIN_TYPE).list();
    }

    /**
     * 根据角色名获取角色信息
     * @param roleType 角色名
     * @return 角色信息
     */
    @Override
    public UserRole getUserRoleByRoleType(String roleType) {
        return this.baseMapper.selectOne(new QueryWrapper<UserRole>()
                .lambda()
                .eq(UserRole::getRoleType, roleType));
    }

    /**
     * 根据 角色id 删除角色
     * @param roleId 角色id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserRole(Integer roleId) {
        UserRole userRole = this.baseMapper.selectById(roleId);
        if(userRole == null){
            return;
        }

        List<WeightRoleRelation> weightRoleRelations = weightRoleRelationMapper.getRelationSbyRoleId(roleId);
        List<Integer> relationIds = weightRoleRelations.stream().map(WeightRoleRelation::getRelationId)
                .collect(Collectors.toList());

        if(!relationIds.isEmpty()){
            //删除权限，角色关系信息
            weightRoleRelationMapper.deleteBatchIds(relationIds);
        }

        //删除角色信息
        this.baseMapper.delete(new UpdateWrapper<UserRole>().lambda()
                .eq(UserRole::getRoleId, roleId)
                .and(wrapper -> wrapper.eq(UserRole::getType, RoleSortType.NORMAL_TYPE)
                        .or()
                        .eq(UserRole::getType, RoleSortType.ADMIN_TYPE)));
    }

    /**
     * 获取全部角色信息
     * @return 全部角色信息
     */
    @Override
    public List<UserRole> getAllUserRoleInfo() {
        return this.baseMapper.selectList(null);
    }

    /**
     * 根据 角色id 获取角色信息
     * @param roleId 角色id
     * @return 获取角色信息
     */
    @Override
    public UserRole getUserRoleById(Integer roleId) {
        return this.baseMapper.selectById(roleId);
    }
}
