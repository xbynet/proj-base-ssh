package net.xby1993.common.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomAuthorizingRealm extends AuthorizingRealm {
	private static final Logger log=LoggerFactory.getLogger(CustomAuthorizingRealm.class);
	
	public CustomAuthorizingRealm() {
		super();
		setAuthenticationTokenClass(UsernamePasswordToken.class);
	}

	/**
	 * 获取当前认证实体的授权信息（授权包括：角色、权限）
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		String username=(String) principals.getPrimaryPrincipal();

		info.setRoles(null);
		info.setStringPermissions(null);
		return info;
	}

	/**
	 * 根据认证方式（如表单）获取用户名称、密码
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
/*		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		if (username == null) {
			log.warn("用户名不能为空");
			throw new AccountException("用户名不能为空");
		}


		try {
			//根据用户名获取用户
		} catch(Exception ex) {
			log.warn("获取用户失败\n" + ex.getMessage());
		}
		if (user == null) {
		    log.warn("用户不存在");
		    throw new UnknownAccountException("用户不存在");
		}
		if(user.getStatus() != null && "0".equals(user.getStatus().value)) {
		    log.warn("用户被禁止使用");
		    throw new UnknownAccountException("用户被禁止使用");
		}
		log.info("用户【" + username + "】正在登录");
		return new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),getName());*/
		return null;
	}
}
