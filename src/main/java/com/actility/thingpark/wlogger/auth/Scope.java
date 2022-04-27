package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.smp.rest.dto.DomainRestrictionsDto;

import java.util.Objects;

/**
 * A scope describe a context, an authenticated use is allowed to access.
 * For example, a SUBSCRIBER scope, allows to retrieve data for a specific subscriber,
 * who's ID is contextId.
 * A scope have an ID that depends on authentication flow used to access thid scope. This
 * ID correspond to the logged user UUID, the subscriber UUID, or the supplier UUID.
 */
public class Scope {
    private final String id;
    private final ScopeType scopeType;
    private final String contextId;
    private final DomainRestrictionsDto domainRestrictions;

    public Scope(String id, ScopeType scopeType, String contextId,
                 DomainRestrictionsDto domainRestrictions) {
        this.id = id;
        this.contextId = contextId;
        this.scopeType = scopeType;
        this.domainRestrictions = domainRestrictions;
    }

    /**
     * A scope ID is assigned when a user logs in. It's value
     * depends on the type of user that identified:
     * For an end-user, this is the user UUID
     * For an admin, this is the network partner UUID or the subscriber UUID
     * For a super admin, this is the admin ID (from database)
     *
     * @return Identifier used to retrieve the scope.
     */
    public String getId() {
        return id;
    }

    public String getContextId() {
        return contextId;
    }

    public ScopeType getScopeType() {
        return scopeType;
    }

    private DomainRestrictionsDto getDomainRestrictions() {
        return domainRestrictions;
    }

    public enum ScopeType {
        SUBSCRIBER,
        NETWORK_PARTNER,
        UNLIMITED
    }

    /**
     * Customer Id is the subscriber ID or *
     *
     * @return Subscriber ID or *
     */
    public String getCustomerId() {
        if (scopeType == ScopeType.SUBSCRIBER) {
            return getContextId();
        }
        return "*";
    }

    public DomainRestrictionsDto getCustomerRestrictions() {
//        if (scopeType == ScopeType.SUBSCRIBER) {
        if (scopeType == ScopeType.SUBSCRIBER || scopeType == ScopeType.UNLIMITED) {  // for test admin local
            return getDomainRestrictions();
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", scopeType, contextId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Scope scope = (Scope) o;
        return Objects.equals(contextId, scope.contextId) &&
                scopeType == scope.scopeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contextId, scopeType);
    }
}
