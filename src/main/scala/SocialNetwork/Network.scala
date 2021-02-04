package SocialNetwork

import scala.collection.immutable.ListMap

abstract class Network{

  //Network Level Functionalities
  def networkMap: Network
  def addPerson(person :Person) : Network
  def removePerson(person :Person) :Network
  def numUsers: Int
  def contains(person:Person):Boolean

  // Node level functionalities
  def friend(personOne : Person , personTwo : Person): Network
  def unfriend(personOne : Person, personTwo : Person) : Network
  def numFriends(person : Person):Int
  def isFriend(personOne:Person, personTwo:Person):Boolean

  // Analytics & Inference based functionalities
  def numUnconnected : Int
  def mostFamous:List[Person]
  def leastFamous: List[Person]
  def friendOf(person : Person) : List[Person]
  def mostFriends: List[Person]
  def leastFriends: List[Person]
  def youngestUser:List[Person]
  def oldestUser : List[Person]
  def similarUsers:Map[String,List[Person]]
  def isDirectConnection(personOne : Person , personTwo : Person):Boolean
  def friendOfFriend(personOne : Person , personTwo : Person):Boolean
  def isConnected(personOne : Person , personTwo : Person):Boolean
  def mutualFriends(personOne : Person , personTwo : Person): List[Person]
  def recommended(person : Person): List[Person]
  def recommendedWithCount(person : Person):ListMap[Person,Int]
  def recommendationGraph:Map[Person,List[Person]]
  def recommendationGraphWithStats: Map[Person,ListMap[Person,Int]]

}

