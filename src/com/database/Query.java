package com.database;

/**
 * Enum for query identifiers
 * and the SQL queries linked to them
 * Created by adminpc on 25/10/2016.
 */
public enum Query {

    GET_ALL_USERS(
            "SELECT idUser AS id, name, date, passwd FROM user;"),
    GET_USER_INFO(
            "SELECT idUser AS id, name, date, passwd FROM user WHERE name = ? OR idUser = ?;"),
    GET_USER_EXISTS_BY_NAME(
            "SELECT COUNT(*) AS NUMBER FROM user WHERE name = ?"),
    GET_USER_EXISTS_BY_ID(
            "SELECT COUNT(*) AS NUMBER FROM user WHERE idUser = ?"),
    CREATE_USER(
            "INSERT INTO user(name, passwd, date) VALUES(?, ?, CURDATE());"),
    REMOVE_USER(
            "DELETE FROM USER WHERE name = ?;"
    ),
    GET_LIST_WORDS(
            "SELECT * FROM word WHERE EXISTS (SELECT * FROM wordEntry WHERE idList = ? AND idWord = word.idWord LIMIT 1);"),
    CREATE_WORD(
            "INSERT INTO word(idWord, word, idLang) VALUES(?, ?, ?);"),
    GET_WORD(
            "SELECT * FROM word WHERE idWord = ?;"),
    GET_WORD_BY_STRING(
            "SELECT * FROM word WHERE word = ?;"),
    DELETE_WORD(
            "DELETE FROM word WHERE idWord = ?;"),
    DELETE_WORD_EXPLANATION(
            "DELETE FROM wordExplanation WHERE idWord = ?;"),
    GET_WORD_MASTERS_IDS(
            "SELECT * FROM wordExplanation WHERE idWord = ?;"),
    SEARCH_WORDS(
            "SELECT word, lang.name AS lang FROM word, language as lang WHERE word LIKE ? AND lang.idLanguage = word.idLang AND lang.idLanguage = ? LIMIT 5;"),
    GET_LANGUAGES(
            "SELECT idLanguage, name FROM language;"
    ),
    CREATE_LIST(
            "INSERT INTO list(idList, name, creator, maxPoints) VALUES(?, ?, ?, ?);"),
    DELETE_LIST(
            "DELETE FROM list WHERE idList = ?;"
    ),
    LIST_EXISTS_BY_NAME("SELECT * FROM list WHERE name = ?;"),
    CREATE_WORDENTRY(
            "INSERT INTO wordEntry(idWord, idList) SELECT word.idWord, list.idList FROM word, list " +
                    "WHERE word.idWord = ? AND list.idList = ?;"),
    DELETE_WORDENTRY(
            "DELETE FROM wordEntry WHERE idList = ?;"
    ),
    GET_LIST(
            "SELECT idList, name, creator, maxPoints FROM list WHERE idList = ?"),
    GET_ALL_LISTS(
            "SELECT idList, name, creator, maxPoints FROM list;"
    ),
    ADD_WORD_TO_LIST(
            "INSERT INTO wordEntry(idWord, idList) VALUES(?, ?);"
    ),
    GET_RESULTS(
            "SELECT * FROM result WHERE idResult = ?;"),
    GET_USER_RESULTS(
            "SELECT * FROM result WHERE idUser = ?;"),
    GET_USER_RESULTS_FROM_TIMEINTERVAL(
            "SELECT * FROM result WHERE idUser = ? AND result.date BETWEEN ? AND ?;"),
    CREATE_RESULT(
            "INSERT INTO result(date, idUser, score, idList) " +
                    "VALUES(?, ?, ?, ?);"),
    CREATE_WORD_EXPLANATION(
            "INSERT INTO wordExplanation(idWord, idMaster) VALUES(?, ?);"),
    GET_MAX_USER_ID("SELECT MAX(idUser) AS max FROM user;"),
    GET_MAX_WORD_ID("SELECT MAX(idWord) AS max FROM word;"),
    GET_MAX_WORDLIST_ID("SELECT MAX(idList) AS max FROM list;");

    // sql string property of the enum member
    private String sqlQuery;

    // constructor
    Query(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    /**
     * returns the SQL query string of the specified query
     *
     * @return sql string
     */
    public String getSQL() {
        return sqlQuery;
    }
}
