package neko.transaction.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.UserWeight;
import neko.transaction.member.mapper.UserWeightMapper;
import neko.transaction.member.service.UserWeightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class UserWeightServiceImpl extends ServiceImpl<UserWeightMapper, UserWeight> implements UserWeightService {
    /**
     * 添加权限
     * @param weightType 权限名
     */
    @Override
    public void newUserWeight(String weightType) {
        if(this.baseMapper.getUserWeightByWeightType(weightType) != null){
            throw new DuplicateKeyException("weightType重复");
        }

        UserWeight userWeight = new UserWeight();
        LocalDateTime now = LocalDateTime.now();
        userWeight.setWeightType(weightType)
                .setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(userWeight);
    }

    /**
     * 分页查询权限信息
     * @param vo 分页查询vo
     * @return 分页查询结果
     */
    @Override
    public Page<UserWeight> getUserWeightByQueryLimitedPage(QueryVo vo) {
        Page<UserWeight> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<UserWeight> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            queryWrapper.lambda().eq(UserWeight::getWeightType, vo.getQueryWords());
        }

        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 查询指定 角色id 未绑定的权限
     * @param roleId 角色id
     * @return 角色id 未绑定的权限
     */
    @Override
    public List<UserWeight> getUnbindWeightByRoleId(Integer roleId) {
        return this.baseMapper.getUnbindUserWeightByRoleId(roleId);
    }

    /**
     * 根据 权限id 删除权限
     * @param weightId 权限id
     */
    @Override
    public void deleteUserWeight(Integer weightId) {
        this.baseMapper.delete(new UpdateWrapper<UserWeight>().lambda()
                .eq(UserWeight::getWeightId, weightId));
    }

    /**
     * 获取全部权限信息
     * @return 全部权限信息
     */
    @Override
    public List<UserWeight> getAllUserWeight() {
        return this.baseMapper.selectList(null);
    }

    /**
     * 根绝 权限id 获取权限信息
     * @param weightId 权限id
     * @return 获取权限信息
     */
    @Override
    public UserWeight getUserWeightById(Integer weightId) {
        return this.baseMapper.selectById(weightId);
    }
}
