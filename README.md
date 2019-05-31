# Advanced Software Engineering project
## Implementation of Identifier-Based context-Dependent API Method Recommendation system

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
* Test Coverage: [Coveralls](https://coveralls.io)


### Installation 

First you need to get the contexts and events, the ones the app is testet with are:

**Contexts dataset:**

http://www.st.informatik.tu-darmstadt.de/artifacts/kave/Contexts-170503.zip

**events dataset:**

http://www.st.informatik.tu-darmstadt.de/artifacts/kave/events-170301-2.zip

Both of them are taken via the original source: http://www.kave.cc/datasets

**Additional info:**

  * **Contexts version:** May 3, 2017

  * **events version:** Jan 18, 2018

### Step by step guide 

#### Step 1 - Preparation: 

The first step you need to do is to download the sources. You either download a preindexed databases or you start from scratch.
Both ways will be described now. The goal is to reach a datastructure that looks like this:

![Structure](docs/images/structure.png)


##### Step 1.1a - Contexts, preindexed Database:

Download the preindexed databases from:  https://gofile.io/?c=akdj6u
If it does not work, please reach out to @dpinezich.

##### Step 1.1b - Contexts, from scratch:

If you like to create your own settings just download the contexts from KAVE (http://www.kave.cc/datasets)
as described. This is also the preferred way if you want to update the contexts. It should look like this:

 ![Structure](docs/images/contexts.png)
 
 This is to be understand additive to the first Image where Data only contains Events.

##### Step 1.2 - Events:

The events can easily be downloaded from the KAVE datasets ((http://www.kave.cc/datasets))


##### Step 1.3 - Jar-file:

You can get the Jar-file directly from GitHub in the mvn-repo branch: https://github.com/Amos94/ASE/tree/mvn-repo


##### Step 1.4 - Finalization:


