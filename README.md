# Advanced Software Engineering project
## Implementation of Identifier-Based Context-Dependent API Method Recommendation system

### Project status:
[![Build Status](https://travis-ci.org/Amos94/ASE.svg?branch=master)](https://travis-ci.org/Amos94/ASE)
[![Coverage Status](https://coveralls.io/repos/github/Amos94/ASE/badge.svg?branch=master)](https://coveralls.io/github/Amos94/ASE?branch=master)

### Description:
**Goal:** Reimplement the system described in the given paper using the KAVE CC datasets.

#### Paper abstract:
Abstract—Reuse recommendation systems support the developer by suggesting useful API methods, classes or code snippets based on code edited in the IDE. Existing systems based on structural information, such as type and method usage, are not effective in case of general purpose types such as String. To alleviate this, we propose a recommendation system based on identifiers that utilizes the developer’s intention embodied in names of variables, types and methods. We investigate the impact of several variation points of our recommendation algorithm and evaluate the approach for recommending methods from the Java and Eclipse APIs in 9 open source systems. Furthermore, we compare our recommendations to those of a structure-based recommendation system and describe a metric for predicting the expected precision of a recommendation. Our findings indicate that our approach performs significantly better than the structure-based approach.

**Keywords:** software reuse; recommendation system; identifier; data mining

### What did we use:
* Programming language: [JAVA 8](https://www.java.com/en/)
* Framework: [SPRING BOOT](https://spring.io/projects/spring-boot)
* Framework: [MAVEN 3](https://maven.apache.org/)
* Datasets: [KAVE CC](http://kave.cc/)
* CI: [TRAVIS CI](https://travis-ci.org)
* Code Coverage: [Coveralls](https://coveralls.io)


### Installation 

First you need to get the contexts and events, the ones the app is testet with are:

**Contexts dataset:**

http://www.st.informatik.tu-darmstadt.de/artifacts/kave/Contexts-170503.zip

**Events dataset:**

http://www.st.informatik.tu-darmstadt.de/artifacts/kave/Events-170301-2.zip

Both of them are taken via the original source: http://www.kave.cc/datasets

**Additional info:**

  * **Contexts version:** May 3, 2017

  * **Events version:** Jan 18, 2018

### How to use it:

