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
            "SELECT idUser AS id, name, date, passwd FROM user WHERE name = ?;"),
    GET_USER_EXISTS_BY_NAME(
            "SELECT COUNT(*) FROM user WHERE name = ?"),
    GET_USER_EXISTS_BY_ID(
            "SELECT COUNT(*) FROM user WHERE idUser = ?"),
    CREATE_USER(
            "INSERT INTO user(name, passwd, date) VALUES(?, ?, CURDATE());"),

    GET_LIST_WORDS(
            "SELECT * FROM word WHERE EXISTS (SELECT * FROM wordEntry WHERE idList = ? AND idWord = word.idWord LIMIT 1);"),
    CREATE_WORD(
            "INSERT INTO word(word, idMaster, idLang) SELECT ?, master.idWord, lang.idLanguage FROM " +
            "word AS master, language AS lang WHERE word.idWord = ? AND lang.idLang = ?;"),
    GET_WORD("SELECT * FROM word WHERE idWord = ?;"),
    GET_WORD_MASTERS_IDS("SELECT * FROM wordExplanation WHERE idWord = ?;"),
    GET_LANGUAGES(
            "SELECT * FROM language;"
    ),

    CREATE_LIST(
            "INSERT INTO list(name, creator) VALUES(?, ?);"),
    CREATE_WORDENTRY(
            "INSERT INTO wordEntry(idWord, idList) SELECT word.idWord, list.idList FROM word, list " +
            "WHERE word.idWord = ? AND list.idList = ?;"),
    GET_LIST(
            "SELECT name, creator FROM list WHERE idList = ?"),
    GET_RESULTS(
            "SELECT * FROM result WHERE idResult = ?;"),
    GET_USER_RESULTS(
            "SELECT * FROM result WHERE idUser = ?;"),
    GET_USER_RESULTS_FROM_TIMEINTERVAL(
            "SELECT * FROM result WHERE idUser = ? AND result.date BETWEEN ? AND ?;"),
    CREATE_RESULT(
            "INSERT INTO result(date, idUser, score, idLang, playTime) " +
            "VALUES(?, ?, ?, ?, ?);");

    // sql string property of the enum member
    private String sqlQuery;

    // constructor
    Query(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    /**
     * returns the SQL query string of the specified query
     * @return sql string
     */
    public String getSQL() {
        return sqlQuery;
    }
}
