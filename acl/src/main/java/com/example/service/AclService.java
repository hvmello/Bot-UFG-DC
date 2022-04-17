package com.example.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AclService {

	@Autowired
	private MutableAclService mutableAclService;

	@Transactional
	public void adiciona(Object objeto) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ObjectIdentity identity = new ObjectIdentityImpl(objeto);
		MutableAcl acl = mutableAclService.createAcl(identity);
		acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, new PrincipalSid(authentication), true);
		acl.insertAce(acl.getEntries().size(), BasePermission.READ, new GrantedAuthoritySid("ROLE_GESTOR"), true);
		mutableAclService.updateAcl(acl);
	}

	
	public boolean hasPermission(Object objeto,List<Permission> permissoes) throws NotFoundException{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ObjectIdentity identity = new ObjectIdentityImpl(objeto);
		Acl acl = mutableAclService.readAclById(identity);
		List<Sid> sids =  new ArrayList<>();
		
		authentication.getAuthorities().forEach(a -> {
			sids.add(new GrantedAuthoritySid(a.getAuthority()));
		} );
		sids.add(new PrincipalSid(authentication));
		
		return acl.isGranted(permissoes, sids, true);
		
	}
	
}