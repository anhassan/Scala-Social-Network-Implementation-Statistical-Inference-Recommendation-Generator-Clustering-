package SocialNetwork

import scala.collection.immutable.ListMap
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import lectures.part3fp.TuplesAndMaps.engine

class SocialNetwork(usersMap: Map[Person, List[Person]]) extends Network {

  override def networkMap: SocialNetwork = new SocialNetwork(this.usersMap)

  override def contains(person: Person): Boolean = if (this.usersMap.contains(person)) true else false

  override def addPerson(person: Person): SocialNetwork =
    new SocialNetwork(this.usersMap + (person -> List[Person]()))

  override def removePerson(person: Person): SocialNetwork = new SocialNetwork(this.usersMap - person)

  override def numFriends(person: Person): Int = this.usersMap(person).size

  override def isFriend(personOne: Person, personTwo: Person): Boolean =
    if (this.usersMap(personOne).contains(personTwo) &&
      this.usersMap(personTwo).contains(personOne)) true
    else false

  override def friendOf(person: Person): List[Person] = {
    this.usersMap.filterKeys(user=>this.usersMap(user).contains(person)).keys.toList
  }

  override def mostFamous: List[Person] = {
    val userFriendsNumber = this.usersMap.map(user=>user._1->friendOf(user._1).size)
    val maxFriendsPerUser = userFriendsNumber.map(_._2).toList.max
    userFriendsNumber.filter(user=>user._2==maxFriendsPerUser).keys.toList
  }

  override def leastFamous: List[Person] = {
    val userFriendsNumber = this.usersMap.map(user=>user._1->friendOf(user._1).size)
    val minFriendsPerUser = userFriendsNumber.map(_._2).toList.min
    userFriendsNumber.filter(user=>user._2==minFriendsPerUser).keys.toList
  }

  override def mostFriends: List[Person] = {
    val numFriendsPerPerson = this.usersMap.map(user => user._1 -> user._2.size)
    val maxNumFriends = numFriendsPerPerson.values.toList.max
    numFriendsPerPerson.filter(user => user._2 == maxNumFriends).map(_._1).toList
  }

  override def leastFriends: List[Person] = {
    val numFriendsPerPerson = this.usersMap.map(user => user._1 -> user._2.size)
    val minNumFriends = numFriendsPerPerson.values.toList.min
    numFriendsPerPerson.filter(user => user._2 == minNumFriends).map(_._1).toList
  }

  override def numUnconnected: Int = {
    val numFriendsPerPerson = this.usersMap.map(user => user._1 -> user._2.size)
    val usersWithZeroFriends = numFriendsPerPerson.filter(p => p._2 == 0)
    usersWithZeroFriends.size
  }

  override def isDirectConnection(personOne: Person, personTwo: Person): Boolean = {
    if (this.usersMap(personOne).contains(personTwo) || personOne == personTwo) true
    else false
  }

  override def friend(personOne: Person, personTwo: Person): SocialNetwork = {
    // Add exceptions
    new SocialNetwork(this.usersMap + (personOne -> (this.usersMap(personOne) :+ personTwo)) +
      (personTwo -> (this.usersMap(personTwo) :+ personOne)))
  }

  override def unfriend(personOne: Person, personTwo: Person): SocialNetwork = {
    // Add exceptions
    if (this.usersMap(personOne).contains(personTwo) && this.usersMap(personTwo).contains(personOne)) {
      new SocialNetwork(this.usersMap + (personOne -> this.usersMap(personOne)
        .filterNot(p => p == personTwo)) + (personTwo -> this.usersMap(personTwo)
        .filterNot(p => p == personOne)))
    }
    else {
      new SocialNetwork(this.usersMap)
    }
  }

  override def numUsers: Int = this.usersMap.size

  override def mutualFriends(personOne: Person, personTwo: Person): List[Person] = {
    if (this.usersMap(personOne).contains(personTwo)) {
      this.usersMap(personTwo).filter(p => this.usersMap(personOne).contains(p))
    } else List[Person]()
  }

  override def friendOfFriend(personOne: Person, personTwo: Person): Boolean = {
    if (isDirectConnection(personOne, personTwo)) true
    else this.usersMap(personOne).filter(p => isDirectConnection(p, personTwo)).size > 0
  }

  override def isConnected(personOne: Person, personTwo: Person): Boolean =
    isDirectConnection(personOne, personTwo) && friendOfFriend(personOne, personTwo)

  override def recommended(person: Person): List[Person] = {
    this.usersMap(person).flatMap(friend => this.usersMap(friend)
      .filter(predicted => !isDirectConnection(person, predicted))).distinct
  }

  override def recommendedWithCount(person: Person): ListMap[Person, Int] = {
    val recommendedPeople = this.usersMap(person).flatMap(friend => this.usersMap(friend)
      .filter(predicted => !isDirectConnection(person, predicted)))
    val recommendedPeopleWithCounts = recommendedPeople.toSeq.groupBy(identity).mapValues(_.size)
    ListMap(recommendedPeopleWithCounts.toSeq.sortWith(_._2 > _._2): _*)
  }

  override def recommendationGraph: Map[Person, List[Person]] = {
    this.usersMap.map(user => user._1 -> recommended(user._1))
  }

  override def recommendationGraphWithStats: Map[Person, ListMap[Person, Int]] = {
    this.usersMap.map(user => user._1 -> recommendedWithCount(user._1))
  }

  override def youngestUser: List[Person] = {
    val minAge = this.usersMap.keys.toList.map(user => user.yearOfBirth.toInt).max
    this.usersMap.keys.toList.filter(user => user.yearOfBirth == minAge.toString)
  }

  override def oldestUser: List[Person] = {
    val maxAge = this.usersMap.keys.toList.map(user => user.yearOfBirth.toInt).min
    this.usersMap.keys.toList.filter(user => user.yearOfBirth == maxAge.toString)
  }

  override def similarUsers: Map[String, List[Person]] = {
    val propertyValues = this.usersMap.map(user => user._1.region).toList.distinct
    val clusters = propertyValues.map(property => property -> this.usersMap
      .filterKeys(user => user.region == property).keys.toList).toMap
    clusters
  }
}


