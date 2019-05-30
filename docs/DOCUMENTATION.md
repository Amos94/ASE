# Cookbook / Documentation
#### Implementation of Identifier-Based context-Dependent API Method Recommendation system

## 1. Introduction

## 2. Technical Description



## 3. DevOps, CI/CD


## 4. Configuration

To make the program as versatile and easy to use when deployed as a jar we added a number of configuration variables. Some of them need to be set directly in Configuration.java (the ones in our opinion are seldom changed) and ones which can directly be implemented while calling java -jar. Here is a list of all settings which concludes what they do and how they can be changed:

Name | Default | Purpose | Accessor
------------ | ------------- | ------------ | -------------
EVENTS_DIR | Data/events | folder for all event zips | Needs to be changed in the file
CONTEXTS_DIR | Data/Contexts | folder for all context zips | Needs to be changed in the file
INDEX_STORAGE | IndexStorage | The location where the created index should be stored | Needs to be changed in the file
MAX_CANDIDATES | 10 | Maximum number of candidates  | Needs to be changed in the file
STOP_WORDS | default list | List of stop words that can be removed | Needs to be changed in the file
RECOMMENDATION_ZIPS | -1 | How many Queries should be taken into account (-1 = all)  | Can be set initially
REMOVE_STOP_WORDS | true | Should the stop words be removed or not | Can be set initially
REINDEX_DATABASE | false | should the database be reindexed (only needed in the first run, or when a new setting is evaluated) | Can be set initially
EVALUATION | true | Should there be an evaluation of the events? => often used if you only want to reindex the db for experiments | Can be set initially
LAST_N_CONSIDERED_STATEMENTS | 3 | How big is the lookback set? | Can be set initially
USE_EVENTS | false | Use events against the whole indexed database for recommendations | Needs to be changed in the file
USE_TEST_CONTEXTS | true | use contexts and query against the filtered by project indexes for recommendations => as described in the paper | Needs to be changed in the file
DELIMITER | ** | Set an individual delimiter for messages | Needs to be changed in the file

This table concludes into 3 flavors of calling the jar and make it configurable, this is in detail described now:

*Minconfig*

This can simply be called by: ```java -jar aseproject-0.0.1-SNAPSHOT.jar```

*Mediumconfig*

This can be called with three additional parameters: ```java -jar aseproject-0.0.1-SNAPSHOT.jar false true false```

The first parameter is: remove stopwords (bool)
The second parameter is: reindex the database (bool)
The third parameter is: do the evaluation (bool)

*Maximumconfig*

The last configuration has the most parameters and extends the mediumconfig: ```java -jar aseproject-0.0.1-SNAPSHOT.jar false true false 6 1```

The first parameter is: remove stopwords (bool)
The second parameter is: reindex the database (bool)
The third parameter is: do the evaluation (bool)
The fourth parameter is: the last n considered statements for the lookback (int)
The fifth parameter is: set number of recommendation zips (int) 

## 5. Evaluation

## 6. Overheads and adaption

## 7. Conclusion

