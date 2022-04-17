package com.example.config;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
public class ConfiguracaoACL {

    @Autowired
    private DataSource dataSource;

    @Bean
    public AclAuthorizationStrategy aclAuthorization() {
        AclAuthorizationStrategyImpl aclAuthorizationStrategy = new AclAuthorizationStrategyImpl(
                new SimpleGrantedAuthority("ROLE_ADMIN"));
        return aclAuthorizationStrategy;
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        PermissionGrantingStrategy grantingStrategy = new DefaultPermissionGrantingStrategy(
                new ConsoleAuditLogger());
        return grantingStrategy;
    }

    @Bean
    public Ehcache ehCache() {
        CacheManager cacheManager = CacheManager.create();
        if (!cacheManager.cacheExists("aclCache")) {
            cacheManager.addCache("aclCache");
        }

        Ehcache ehCache = cacheManager.getEhcache("aclCache");
        return ehCache;
    }


    @Bean
    public AclCache aclCache(Ehcache ehCache, PermissionGrantingStrategy grantingStrategy,
                             AclAuthorizationStrategy aclAuthorizationStrategy) {
        AclCache aclCache = new EhCacheBasedAclCache(ehCache, grantingStrategy,
                aclAuthorizationStrategy);

        return aclCache;
    }

    @Bean
    public BasicLookupStrategy basicLookupStrategy(AclCache aclCache,
                                                   AclAuthorizationStrategy aclAuthorizationStrategy,
                                                   PermissionGrantingStrategy grantingStrategy) {

        return new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy,
                grantingStrategy);
    }

    @Bean
    public MutableAclService mutableAclService(BasicLookupStrategy lookupStrategy, AclCache aclCache) {
        JdbcMutableAclService service = new JdbcMutableAclService(dataSource, lookupStrategy,
                aclCache);
        service.setClassIdentityQuery("select currval(pg_get_serial_sequence('acl_class', 'id'))");
        service.setSidIdentityQuery("select currval(pg_get_serial_sequence('acl_sid', 'id'))");
        return service;
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler(PermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }

    @Bean
    public PermissionEvaluator permissionEvaluator(MutableAclService aclService) {
        AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService);
        return aclPermissionEvaluator;
    }

}