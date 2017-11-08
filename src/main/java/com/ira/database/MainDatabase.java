package com.ira.database;

import com.ira.entity.AltEntryEntity;
import com.ira.entity.EntryEntity;
import com.ira.entity.GameEntity;
import com.ira.entity.UserEntity;
import com.ira.model.Entry;
import com.ira.model.Game;
import com.ira.model.User;

import java.util.Collection;
import java.util.UUID;

public interface MainDatabase {

    User getUserByID(UUID id);
    User getUser(String username);
    UserEntity createUser(User user);
    UserEntity updateUser(User user);
    int deleteUser(UUID id);
    Collection<User> getUsers();

    Game getGame(UUID id);
    GameEntity createGame(Game game);
    int deleteGame(UUID id);
    Game buildGame(GameEntity gameEntity);
    GameEntity buildGameEntity(Game game);
    Collection<Game> getGames();
    Collection<Game> getGamesByTitle(String title);
    GameEntity updateGame(Game newGame);

    Entry getEntry(UUID id);
    Entry buildEntry(EntryEntity entryEntity);
    EntryEntity buildEntryEntity(Entry entry);
    EntryEntity createEntry(Entry entry);
    int deleteEntry(UUID id);
    Entry getEntry(UUID userId, UUID gameId);
    Collection<Entry> getEntries();

    Collection<AltEntryEntity> getEntriesAlt();

}
