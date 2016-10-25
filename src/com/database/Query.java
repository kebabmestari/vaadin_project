package com.database;

/**
 * Enum for query identifiers
 * Created by adminpc on 25/10/2016.
 */
public enum Query {

    GET_USER_INFO("SELECT idUser AS id, name, date, passwd FROM user WHERE name = ?;"),
    GET_USER_EXISTS("SELECT COUNT(*) FROM user WHERE name = ?"),
    CREATE_USER("INSERT INTO user(name, passwd) VALUES(?, ?, CURDATE());"),

    GET_WORDS("SELECT * FROM word WHERE EXISTS (SELECT * FROM wordEntry WHERE idList = ? AND idWord = word.idWord LIMIT 1);"),
    CREATE_WORD("INSERT INTO word(word, idMaster, idLang) SELECT ?, master.idWord, lang.idLanguage FROM " +
            "word AS master, language AS lang WHERE word.idWord = ? AND lang.idLang = ?;"),

    CREATE_LIST("INSERT INTO list(name, creator) VALUES(?, ?);"),
    CREATE_WORDENTRY("INSERT INTO wordEntry(idWord, idList) SELECT word.idWord, list.idList FROM word, list " +
            "WHERE word.idWord = ? AND list.idList = ?;"),

    GET_RESULTS("SELECT * FROM result WHERE idResult = ?;"),
    GET_USER_RESULTS("SELECT * FROM result WHERE idUser = ?;"),
    GET_USER_RESULTS_FROM_TIMEINTERVAL("SELECT * FROM result WHERE idUser = ? AND result.date BETWEEN ? AND ?;"),
    CREATE_RESULT("INSERT INTO result(date, idUser, score, idLang, playTime) " +
            "VALUES(?, ?, ?, ?, ?);");

    private String sqlQuery;

    Query(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    /**
     * ToString returns the SQL query string of the specified query
     * @return sql string
     */
    @Override
    public String toString() {
        return sqlQuery;
    }
}
