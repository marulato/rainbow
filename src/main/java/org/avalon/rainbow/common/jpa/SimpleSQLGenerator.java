package org.avalon.rainbow.common.jpa;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.utils.StringUtils;

public class SimpleSQLGenerator extends AbstractSQLGenerator {

    private static final Cache<String, String> sqlCache = CacheBuilder.newBuilder().build();
    private static final Cache<String, ORMEntity> ormCache = CacheBuilder.newBuilder().build();

    @Override
    public String insert(BasePO entity) {
        ORMEntity ormEntity = getORM(entity);
        String sql = sqlCache.getIfPresent(ormEntity.getTableName() + ":INSERT");
        if (StringUtils.isBlank(sql)) {
            String insert = "INSERT INTO " + ormEntity.getTableName() +
                    insertClause(ormEntity);
            sqlCache.put(ormEntity.getTableName() + ":INSERT", insert);
            return insert;
        }
        return sql;
    }

    @Override
    public String update(BasePO entity) {
        ORMEntity ormEntity = getORM(entity);
        String sql = sqlCache.getIfPresent(ormEntity.getTableName() + ":UPDATE");
        if (StringUtils.isBlank(sql)) {
            String update = "UPDATE " + ormEntity.getTableName() +
                    updateClause(ormEntity);
            sqlCache.put(ormEntity.getTableName() + ":UPDATE", update);
            return update;
        }
        return sql;
    }

    @Override
    public String delete(BasePO entity) {
        ORMEntity ormEntity = getORM(entity);
        String sql = sqlCache.getIfPresent(ormEntity.getTableName() + ":DELETE");
        if (StringUtils.isBlank(sql)) {
            String delete = "DELETE FROM " + ormEntity.getTableName() +
                    whereClause(ormEntity);
            sqlCache.put(ormEntity.getTableName() + ":DELETE", delete);
            return delete;
        }
        return sql;
    }

    private ORMEntity getORM(BasePO entity) {
        ORMEntity ormEntity = ormCache.getIfPresent(entity.getClass().getName());
        if (ormEntity == null) {
            ormEntity = ORMEntity.getInstance(entity.getClass());
            ormCache.put(entity.getClass().getName(), ormEntity);
        }
        return ormEntity;
    }
}
