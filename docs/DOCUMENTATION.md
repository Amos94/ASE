# Cookbook / Documentation
#### Implementation of Identifier-Based context-Dependent API Method Recommendation system

## 1. Introduction

## 2. Technical Description

In order to replicate the recommender system described in the paper, the development approach was straight forward. After getting used to the Contexts and Events datasets provided by KAVE we decided to start build the visitors. It might not be a surprise that the most used pattern it the project was the visitor pattern. We used it to traverse the syntax trees and retrieve the relevant information needed to create and query the index. The next step was to design and implement the data model of the index. In the end we implemented two versions for querying and evaluate it.

In the beginning, we thought it would be great to imply a test-driven development methodology, but it was a bit of an overhead given that we didn’t know how to interact with KAVE properly. However, we tested the most critical paths of our application and the Jaccard calculation.

Although we have taken extensive care to have implemented just clean code, in later stages, the project needed some deep refactoring. We had to remove code duplication and started to generalize the concepts a bit. Furthermore, we removed the smells and the bugs indicated by our static analysis tool as they were found. In order to keep the code clean and under control, we employed a PR driven methodology (details in the CI/CD section), where at least another peer had to review and approve/ reject the branch before merging master. Hence, we did not have any major problems, we assured that the tests were building and the test coverage increases or stagnates.

We tried to extend the project as much as we could. For example, even if in the paper the similarity measure used to evaluate each recommendation was the Jaccard similarity measure, we also implemented other similarity measurements, such as  Cosine similarity or the Euclidean Distance, so they can be used in later stages in our evaluation or future releases.

Among libraries, we used it some of them worth to be mentioned, e.g., the **Apache Lucene** which also provided the Potter stemmer out of the box or **Simmetrics** which provided a variety of similarity measures ready to use. We used JUnit and Mockito to ease our testing efforts.

In our development we used: Java 8, Spring Boot and MAVEN. As a database solution we chose SQLite because it’s lightweight, but powerful enough to store the index and query it.

For a detailed architectural view, please follow the links below where you can find the UML diagrams of the project packages.

Description | Image Link
------------ | -------------
Top-Level package | [High-Res Link](images/Top-Level Package.png)
Package aseproject | [High-Res Link](images/Package aseproject.png)
Package context | [High-Res Link](images/Package context.png)
Package events | [High-Res Link](images/Package events.png)
Package index | [High-Res Link](images/Package index.png)
Package similarity_measures | [High-Res Link](images/Package similarity_measures.png)
Package visitors | [High-Res Link](images/Package visitors.png)
Package utils | [High-Res Link](images/Package utils.png)
Project dependecies | [High-Res Link](images/mdl-aseproject.png)

For insights in the several classes we programmed, please have a look at the provided javadoc. We tried to add Javadoc to each class and method to make it easier to understand for the reader but not over-exceeding this documentation.

## 3. DevOps, CI/CD



## 4. Configuration

To make the program as versatile and easy to use when deployed as a jar we added a number of configuration variables. Some of them need to be set directly in Configuration.java (the ones in our opinion are seldom changed) and ones which can directly be implemented while calling java -jar. Here is a list of all settings which concludes what they do and how they can be changed:

Name | Default | Purpose | Accessor
------------ | ------------- | ------------ | -------------
EVENTS_DIR | Data/events | Folder for all event zips | Needs to be changed in the file
CONTEXTS_DIR | Data/Contexts | Folder for all context zips | Needs to be changed in the file
INDEX_STORAGE | IndexStorage | The location where the created index should be stored | Needs to be changed in the file
MAX_CANDIDATES | 10 | Maximum number of candidates  | Needs to be changed in the file
STOP_WORDS | default list | List of stop words that can be removed | Needs to be changed in the file
RECOMMENDATION_ZIPS | -1 | How many Queries should be taken into account (-1 = all)  | Can be set initially
REMOVE_STOP_WORDS | true | Should the stop words be removed or not | Can be set initially
REINDEX_DATABASE | false | Should the database be reindexed (only needed in the first run, or when a new setting is evaluated) | Can be set initially
EVALUATION | true | Should there be an evaluation of the events? => often used if you only want to reindex the db for experiments | Can be set initially
LAST_N_CONSIDERED_STATEMENTS | 3 | How big is the lookback set? | Can be set initially
USE_EVENTS | false | Use events against the whole indexed database for recommendations | Needs to be changed in the file
USE_TEST_CONTEXTS | true | Use contexts and query against the filtered by project indexes for recommendations => as described in the paper | Needs to be changed in the file
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

### A conceptual problem
The recommender system described in the paper is creating an index to which associates each method call to the its context given a predefined lookback. Then, the recommender is querying the index to retrieve the recommendation set. 

In the specifications given it is stated that we shall build a model from the contexts given and test it against the events. Hence, here we encounter our first conceptual problem, where we tried to link the projects indexed from the contexts to the ones that exist in events. It was impossible. Therefore, we decided to have two versions that can be easily interchanged. 

In the first version, we index the contexts and also use contexts to query the database, where the query filters the the entries stored by the project name. In this case, the results were very close to the one reported in the given paper.

The second version, uses the events to query the stored index (made from contexts), but we do not filter for anything, hence the recommendation rate is considerably smaller than the recommendation rate in the first version since it is impossible to map the project names between the two datasets and we need to query the whole database and retrieve the recommendation sets.

### Getting used with KAVE CC
Nobody can argue the quality of KAVE. The datasets can be easily used for research by familiarized users. The examples provided in the project’s GitHub are very useful for novice users and they provide a very good starting base. However, the lack of documentation was felt in the later stages of the project when we weren’t sure what is there or not. Nonetheless, it wasn’t a big blocker since the development process got easier with time. 

## 7. Reccomendations

### How to use the Recommender
In our opinion the best and the most efficient way to use the recommender is the way described in the paper:
* We already have indexed the contexts for a predefined lookback of 3,4 and 5 for both cases: including and excluding the stopwords
* Add in the Contexts folder a project folder with a zip, i.e. the methods that you want to test. Our recommender will give you a recommendation set for each method found in the SST found in that project.
* Make sure you make the the relevant changes for the case you want to test, e.g., REINDEX_DATABASE = false, EVALUATION = true, lookback should have the same number as the one that was used to index the db, REMOVE_STOPWORDS should have the same value as the one it was used to index the db (due to we have to treat the query in the same way we treated the index at its creation).

You can test against one or more contexts, just make sure you change the RECOMMENDATION_ZIPS value (See Configuration for more information). Furthermore, feel free to change the MAX_CANDIDATES value. In our tests we used a set of 10 methods per recommendation set. It is important to state that the paper does not specify anything about the recommendation set size.

One can also test it against the events. Make sure you take care of the last point above and set the proper configuration. Just add a folder which contains a zip with some events in Events directory. Make sure you will be patient because for each method we need to query the whole database to retrieve the relevant info since it’s impossible to map a project from Contexts to a project from Events. The recommendation rate will be smaller, but at least it’s an interesting exercise to see how powerful is this recommender with unknown contexts.



## 7. Conclusion

