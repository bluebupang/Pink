package com.ssy.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ssy.pink.bean.weibo.EmotionInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EMOTION_INFO".
*/
public class EmotionInfoDao extends AbstractDao<EmotionInfo, String> {

    public static final String TABLENAME = "EMOTION_INFO";

    /**
     * Properties of entity EmotionInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Category = new Property(0, String.class, "category", false, "CATEGORY");
        public final static Property Common = new Property(1, boolean.class, "common", false, "COMMON");
        public final static Property Hot = new Property(2, boolean.class, "hot", false, "HOT");
        public final static Property Icon = new Property(3, String.class, "icon", false, "ICON");
        public final static Property Phrase = new Property(4, String.class, "phrase", false, "PHRASE");
        public final static Property Type = new Property(5, String.class, "type", false, "TYPE");
        public final static Property Url = new Property(6, String.class, "url", false, "URL");
        public final static Property Value = new Property(7, String.class, "value", true, "VALUE");
    }


    public EmotionInfoDao(DaoConfig config) {
        super(config);
    }
    
    public EmotionInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EMOTION_INFO\" (" + //
                "\"CATEGORY\" TEXT," + // 0: category
                "\"COMMON\" INTEGER NOT NULL ," + // 1: common
                "\"HOT\" INTEGER NOT NULL ," + // 2: hot
                "\"ICON\" TEXT," + // 3: icon
                "\"PHRASE\" TEXT," + // 4: phrase
                "\"TYPE\" TEXT," + // 5: type
                "\"URL\" TEXT," + // 6: url
                "\"VALUE\" TEXT PRIMARY KEY NOT NULL UNIQUE );"); // 7: value
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EMOTION_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EmotionInfo entity) {
        stmt.clearBindings();
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(1, category);
        }
        stmt.bindLong(2, entity.getCommon() ? 1L: 0L);
        stmt.bindLong(3, entity.getHot() ? 1L: 0L);
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(4, icon);
        }
 
        String phrase = entity.getPhrase();
        if (phrase != null) {
            stmt.bindString(5, phrase);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(6, type);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(7, url);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(8, value);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EmotionInfo entity) {
        stmt.clearBindings();
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(1, category);
        }
        stmt.bindLong(2, entity.getCommon() ? 1L: 0L);
        stmt.bindLong(3, entity.getHot() ? 1L: 0L);
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(4, icon);
        }
 
        String phrase = entity.getPhrase();
        if (phrase != null) {
            stmt.bindString(5, phrase);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(6, type);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(7, url);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(8, value);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7);
    }    

    @Override
    public EmotionInfo readEntity(Cursor cursor, int offset) {
        EmotionInfo entity = new EmotionInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // category
            cursor.getShort(offset + 1) != 0, // common
            cursor.getShort(offset + 2) != 0, // hot
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // icon
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // phrase
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // type
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // url
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // value
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EmotionInfo entity, int offset) {
        entity.setCategory(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCommon(cursor.getShort(offset + 1) != 0);
        entity.setHot(cursor.getShort(offset + 2) != 0);
        entity.setIcon(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPhrase(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setType(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUrl(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setValue(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final String updateKeyAfterInsert(EmotionInfo entity, long rowId) {
        return entity.getValue();
    }
    
    @Override
    public String getKey(EmotionInfo entity) {
        if(entity != null) {
            return entity.getValue();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EmotionInfo entity) {
        return entity.getValue() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
