package com.project.commons.service;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.commons.mapper.UserMapper;
import com.project.commons.model.User;
/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Service
@AllArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {

    private UserMapper userMapper;

    /**
     * 用户登录
     * @param user
     * @return
     */
    public User selectByLogin(User user) {
        // 验证用户登录
        // 创建查询对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("user_name", user.getUserName());
        // 将密码进行MD5算法加密后进行查询
        queryWrapper.eq("password", SecureUtil.md5(user.getPassword()));
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 分页查询所有用户数据
     * @param user
     * @return
     */
    public IPage<User> getListByPage(User user, Integer page, Integer limit) {
        // 创建查询条件对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StrUtil.isBlankOrUndefined(user.getUserType()), "user_type", user.getUserType());
        // 如果是查询管理员信息, 则不查询当前登录的管理员
        if("0".equals(user.getUserType())) {
            queryWrapper.ne("id", user.getId());
        }
        // 根据姓名查询
        queryWrapper.like(!StrUtil.isBlankOrUndefined(user.getRealName()), "real_name", user.getRealName());
        // 根据学号查询
        queryWrapper.like(!StrUtil.isBlankOrUndefined(user.getStudentNo()), "student_no", user.getStudentNo());
        queryWrapper.like(!StrUtil.isBlankOrUndefined(user.getPhone()), "phone", user.getPhone());
        // 创建page对象
        IPage<User> pageBean = new Page<User>(page, limit);
        return userMapper.selectPage(pageBean, queryWrapper);
    }

    /**
     * 新增或修改用户信息
     * @param user
     * @return
     */
    public boolean insertOrUpdate(User user) {
        // 判断该用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName());
        User userNameSelect = userMapper.selectOne(queryWrapper);
        if(userNameSelect != null) {
            throw new RuntimeException("用户名已存在！");
        }
        // 如果是学生的话, 还要判断学号是否已存在
        if("2".equals(user.getUserType())) {
            QueryWrapper<User> queryWrapperUser = new QueryWrapper<>();
            queryWrapperUser.eq("student_no", user.getStudentNo());
            User studentNoSelect = userMapper.selectOne(queryWrapperUser);
            if(user.getId() == null && studentNoSelect != null) {
                throw new RuntimeException("学号已存在！");
            } else if(user.getId() != null && studentNoSelect.getId() != user.getId()) {
                throw new RuntimeException("学号已存在！");
            }
        }
        // 加密密码
        if(!StrUtil.isBlankOrUndefined(user.getPassword())) {
            user.setPassword(SecureUtil.md5(user.getPassword()));
        }
        return this.saveOrUpdate(user);
    }

    /**
     * 根据ID删除
     * @param ids
     * @return
     */
    public boolean deleteInfo(String ids) {
        // 调用删除
        return this.removeByIds(StrUtil.split(ids, ',', true, true));
    }

    /**
     * 根据条件查询用户信息
     * @param queryWrapper
     * @return
     */
    public List<User> getListDataByParam(QueryWrapper<User> queryWrapper) {
        return userMapper.selectList(queryWrapper);
    }
}
