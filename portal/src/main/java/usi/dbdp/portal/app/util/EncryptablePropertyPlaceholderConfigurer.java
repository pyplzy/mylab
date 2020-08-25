package usi.dbdp.portal.app.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @Package usi.inas.execute.util
 * @ClassName EncryptablePropertyPlaceholderConfigurer
 * @Description 配置文件密文
 * @author zhang.hao2
 * @date 2017年5月17日
 * @version v1.0
 * @copyright (c) 2017 by 安徽科大国创云网科技有限公司.
 */
public class EncryptablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private static final String DB_USER = "jdbc.username";
	private static final String DB_PASS = "jdbc.password";
	private static final String MONGO_USER = "mongo.user";
	private static final String MONGO_PASS = "mongo.pwd";
	private static final String REDIS_PASS = "redis.pass";

	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		try {
			//REDIS密码
			String mRedisPass = props.getProperty(REDIS_PASS);
			if(mRedisPass!=null){
				props.setProperty(REDIS_PASS, FilePasswdUtil.decrypt(mRedisPass));
			}
			//数据库用户名
			String mDbUser = props.getProperty(DB_USER);
			if(mDbUser!=null){
				props.setProperty(DB_USER, FilePasswdUtil.decrypt(mDbUser));
			}
			//数据库密码
			String mDbPass = props.getProperty(DB_PASS);
			if(mDbPass!=null){
				props.setProperty(DB_PASS, FilePasswdUtil.decrypt(mDbPass));
			}
			//MONGODB 用户名
			String mMongoUser = props.getProperty(MONGO_USER);
			if(mMongoUser!=null){
				props.setProperty(MONGO_USER, FilePasswdUtil.decrypt(mMongoUser));
			}
			//MONGODB 密码
			String mMongoPass = props.getProperty(MONGO_PASS);
			if(mMongoPass!=null){
				props.setProperty(MONGO_PASS, FilePasswdUtil.decrypt(mMongoPass));
			}
			super.processProperties(beanFactory, props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
