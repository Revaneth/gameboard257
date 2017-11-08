package com.ira.database;

import com.google.common.collect.Lists;
import com.ira.entity.AltEntryEntity;
import com.ira.entity.EntryEntity;
import com.ira.entity.GameEntity;
import com.ira.entity.UserEntity;
import com.ira.model.Entry;
import com.ira.model.Game;
import com.ira.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
public class PostgresqlDB implements MainDatabase {
private static Logger logger = LoggerFactory.getLogger(PostgresqlDB.class);

    private static final String HOST = "ec2-176-34-111-152.eu-west-1.compute.amazonaws.com";
    private static final int PORT = 5432;
    private static final String DATABASE = "d8hb0oegv928r7";
    private static final String USER_NAME = "jviqabybtmxnsq";
    private static final String PASSWORD = "a7f616021e899a0165fcc61d41c2da5eeb1af5c529eac043364072f83563481b";

    private static EntityManager entityManager;
    private static  EntityManager getEntityManager() {
       if (entityManager == null) {
           String dbUrl = "jdbc:postgresql://" + HOST + ':' + PORT + "/" + DATABASE + "?sslmode=require";

            Map<String, String> properties = new HashMap<String, String>();

            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("hibernate.connection.url", dbUrl);
            properties.put("hibernate.connection.username", USER_NAME);
            properties.put("hibernate.connection.password", PASSWORD);
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");
            properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false"); //PERFORMANCE TIP!
            properties.put("hibernate.hbm2ddl.auto", "update"); //update schema for entities (create tables if not exists)

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myUnit",properties);
            entityManager = emf.createEntityManager();
        }
        return entityManager;
    }

    ////////////////////////////////Users//////////////////////////////////

    @Override
    public User getUserByID(UUID sid) {
        UserEntity userEntity = getEntityManager()
                .find(UserEntity.class, sid);
        if (userEntity != null) {
            return buildUser(userEntity);
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        try {

            Query query = getEntityManager()
                    .createNativeQuery("SELECT * FROM users WHERE username ='"
                            + username + "';", UserEntity.class);
            Object obj;
            obj = query.getSingleResult();
            if (obj != null)
                return buildUser((UserEntity) obj);
            return null;
        } catch (Exception x) {
            return null;
        }
    }

    @Override
    public UserEntity createUser(User user) {
        UserEntity entity = buildUserEntity(user);

        try {
            getEntityManager().getTransaction().begin();

            // Operations that modify the database should come here.
            getEntityManager().persist(entity);

            getEntityManager().getTransaction().commit();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
        return entity;

         //     //  User(entity.getId(), entity.getUsername(), entity.getFirstName(),
         //       entity.getLastName(),entity.getEmail(),entity.getPassword());
    }

    @Override
    public UserEntity updateUser(User user) {

        try {
            UserEntity entity = buildUserEntity(user);
            UserEntity fromDB = getEntityManager().find(UserEntity.class, entity.getId());
            getEntityManager().getTransaction().begin();
            fromDB.setFirstName(entity.getFirstName());
            fromDB.setLastName(entity.getLastName());
            fromDB.setEmail(entity.getEmail());
            fromDB.setPassword(entity.getPassword());
            getEntityManager().getTransaction().commit();
            return fromDB;

        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }

    }

    @Override
    public Collection<User> getUsers() {
        Query query = getEntityManager().createNamedQuery("users.findAll");
        List<UserEntity> resultList = query.getResultList();

        List<User> list = Collections.emptyList();

        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());

            for (UserEntity user : resultList) {
                list.add(buildUser(user));
            }
        }
        return list;
    }

    @Override
    public int deleteUser(UUID id) {
        UserEntity userEntity = getEntityManager().find(UserEntity.class, id);
        if (userEntity != null) {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(userEntity);
            getEntityManager().getTransaction().commit();
            return 0;
        }
        return 1;
    }

    private User buildUser(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getUsername(),
                userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getEmail(), userEntity.getPassword());
    }

    private UserEntity buildUserEntity(User user) {
        return new UserEntity(user.getUserId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.getPassword());
    }

    ////////////////////////////////Games//////////////////////////////////

    @Override
    public Game getGame(UUID id) {
        GameEntity gameEntity = getEntityManager().find(GameEntity.class, id);
        if (gameEntity != null) {
            return buildGame(gameEntity);
        }
        return null;
    }

    @Override
    public GameEntity createGame(Game game) {
        GameEntity entity = buildGameEntity(game);
        try {
            getEntityManager().getTransaction().begin();
            // Operations that modify the database should come here.
            getEntityManager().persist(entity);
            getEntityManager().getTransaction().commit();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
        return entity;
    }

    @Override
    public int deleteGame(UUID id) {
        GameEntity gameEntity = getEntityManager().find(GameEntity.class, id);
        if (gameEntity != null) {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(gameEntity);
            getEntityManager().getTransaction().commit();
            return 0;
        }
        return 1;
    }

    @Override
    public Game buildGame(GameEntity gameEntity) {
        return new Game( gameEntity.getId(),
                        gameEntity.getTitle(),
                        gameEntity.getType()); // mapowanie
    }

    @Override
    public GameEntity buildGameEntity(Game game) {
        return new GameEntity(game.getGameId(),
                            game.getTitleOfGame(),
                            game.getTypeOfGame());
    }

    @Override
    public Collection<Game> getGames() {
        Query query = getEntityManager().createNamedQuery("games.findAll");
        List<GameEntity> resultList = query.getResultList();
        List<Game> list = Collections.emptyList();
        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());
            for (GameEntity game : resultList) {
                list.add(buildGame(game));
            }
        }
        return list;
    }

    ////////////////////////////////Entries//////////////////////////////////

    @Override
    public Entry getEntry(UUID id) {
            EntryEntity entryEntity = getEntityManager().find(EntryEntity.class, id);
            if (entryEntity != null) {
                return buildEntry(entryEntity);
            }
        return null;
    }

    @Override
    public Entry buildEntry(EntryEntity entryEntity) {
            return new Entry( entryEntity.getId(),
                    entryEntity.getUserId(),
                    entryEntity.getGameId()); // mapowanie
        }

    @Override
    public EntryEntity buildEntryEntity(Entry entry) {
        return new EntryEntity(entry.getId(),
                entry.getUserId(),
                entry.getGameId());
    }

    @Override
    public EntryEntity createEntry(Entry entry) {
        EntryEntity entity = buildEntryEntity(entry);
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(entity);
            getEntityManager().getTransaction().commit();
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
        return entity;
    }

    @Override
    public int deleteEntry(UUID id) {
        EntryEntity entryEntity = getEntityManager().find(EntryEntity.class, id);
        if (entryEntity != null) {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(entryEntity);
            getEntityManager().getTransaction().commit();
            return 0;
        }
        return 1;
    }

    @Override
    public Collection<Entry> getEntries() {
        Query query =  getEntityManager().createNamedQuery("entries.findAll");
            List<EntryEntity> resultList = query.getResultList();
            List<Entry> list = Collections.emptyList();
            if (resultList != null && !resultList.isEmpty()) {
                list = Lists.newArrayListWithCapacity(resultList.size());
                for (EntryEntity entryEntity : resultList) {
                    list.add(buildEntry(entryEntity));     }
            }
            return list;
        }

    @Override
    public Entry getEntry(UUID userId, UUID gameId) {
        try {
            logger.info("\n\n\n\n\n\n\n\n  ");
            Query query = getEntityManager()
                    .createNativeQuery("SELECT * FROM entries WHERE userid = '"+userId
                             +"' AND gameid = '"+gameId + "' ;", EntryEntity.class);
            Object obj;
            obj = query.getSingleResult();
            if (obj != null)  return buildEntry((EntryEntity) obj);
            return null;
        }catch (Exception x){return null;}
    }

    @Override
    public Collection<Game> getGamesByTitle(String title) {
        Query query = getEntityManager()
                .createNativeQuery("SELECT * FROM games WHERE title = '"+title+ "';", GameEntity.class);
        List<GameEntity> resultList = query.getResultList();
        List<Game> list = Collections.emptyList();
        if (resultList != null && !resultList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(resultList.size());
            for (GameEntity gameEntity : resultList) {
                list.add(buildGame(gameEntity));
            }
        }
        return list;
    }

    @Override
    public GameEntity updateGame(Game newGame) {
       
            try {
                GameEntity entity = buildGameEntity(newGame);
                GameEntity fromDB = getEntityManager().find(GameEntity.class, entity.getId());
                getEntityManager().getTransaction().begin();
                fromDB.setTitle(entity.getTitle());
                fromDB.setType(entity.getType());
                getEntityManager().getTransaction().commit();
                return fromDB;

            } finally {
                if (getEntityManager().getTransaction().isActive()) {
                    getEntityManager().getTransaction().rollback();
                }
            }
   }

    @Override
    public Collection<AltEntryEntity> getEntriesAlt() {
        Query query = getEntityManager()
                .createNativeQuery(
                        "SELECT e.entryid, u.username, g.title " +
                                " FROM users u, games g, entries e " +
                                " WHERE e.gameid = g.gameid "+
                                " AND e.userid = u.userid;"
                       , AltEntryEntity.class);
        List<AltEntryEntity> resultList = query.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList;
        }
        return null;
    }


} //class end
