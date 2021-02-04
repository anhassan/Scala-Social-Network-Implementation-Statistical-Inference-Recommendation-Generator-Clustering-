package SocialNetwork

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.theSameElementsAs

import scala.collection.immutable.ListMap

class SocialNetworkTest extends AnyFunSuite {

  val person1 = Person(1, "Alex", "1987", 3, "Alberta")
  val person2 = Person(2, "Bob", "1976", 2, "Winnipeg")
  val person3 = Person(3, "Mike", "1994", 4, "Toronto")
  val person4 = Person(4, "Peter", "2007", 3, "Alberta")
  val person5 = Person(5, "John", "1962", 2, "Vancouver")
  val person6 = Person(6, "Tom", "2002", 1, "Toronto")

  val networkMap = Map(person1 -> List(person4, person5, person3), person2 -> List(person3),
    person3 -> List(person2, person5), person4 -> List(person1, person5),
    person5 -> List(person1, person3, person4), person6 -> List[Person]())

  val socialNetwork = new SocialNetwork(networkMap)

  test("numUsers") {
    assert(socialNetwork.numUsers === 6)
  }

  test("contains") {

    val newPerson = Person(7, "Arthur", "2002", 2, "Milton")
    assert(networkMap.contains(person2) === true)
    assert(networkMap.contains(newPerson) === false)

  }

  test("addPerson") {

    val newPerson = Person(7, "Mary", "2001", 2, "NorthYork")
    val updatedSocialNetwork = socialNetwork.addPerson(newPerson)
    assert(updatedSocialNetwork.contains(newPerson) === true)
    assert(updatedSocialNetwork.numUsers === 7)

  }

  test("removePerson") {

    val updatedSocialNetwork = socialNetwork.removePerson(person1)
    assert(socialNetwork.contains(person1) === true)
    assert(updatedSocialNetwork.numUsers === 5)
    assert(updatedSocialNetwork.contains(person1) === false)

  }

  test("isFriend") {

    assert(socialNetwork.isFriend(person1, person4) === true)
    assert(socialNetwork.isFriend(person1, person6) === false)

  }

  test("numFriends") {

    assert(socialNetwork.numFriends(person3) === 2)
    assert(socialNetwork.numFriends(person5) === 3)
    assert(socialNetwork.numFriends(person6) === 0)

  }

  test("unfriend"){

  }

  test("mutualFriends") {

    val mutualFriendsSet1 = socialNetwork.mutualFriends(person1, person4)
    val mutualFriendsSet2 = socialNetwork.mutualFriends(person1, person5)
    val mutualFriendsSet3 = socialNetwork.mutualFriends(person2, person6)

    assert(mutualFriendsSet1.size === 1)
    assert(mutualFriendsSet1 === Seq(person5))
    assert(mutualFriendsSet2.size === 2)
    assert(mutualFriendsSet2 === Seq(person3, person4))
    assert(mutualFriendsSet3.size === 0)

  }

  test("numUnconnected"){
    assert(socialNetwork.numUnconnected===1)
  }

  test("mostFamous"){
    assert(socialNetwork.mostFamous === Seq(person5,person3))
  }

  test("leastFamous"){
    assert(socialNetwork.leastFamous === Seq(person6))
  }

  test("friendsOf"){

    assert(socialNetwork.friendOf(person1) === Seq(person5,person4))
    assert(socialNetwork.friendOf(person3) === Seq(person1,person5,person2))
    assert(socialNetwork.friendOf(person6) === List[Person]())

  }

  test("mostFriends"){
   assert(socialNetwork.mostFriends === Seq(person1,person5))
  }

  test("leastFriends"){
    assert(socialNetwork.leastFriends === Seq(person6))
  }

  test("yongestUser"){
    assert(socialNetwork.youngestUser === Seq(person4))
  }

  test("oldestUser"){
    assert(socialNetwork.oldestUser === Seq(person5))
  }

  test("friendOfFriend") {

    assert(socialNetwork.friendOfFriend(person1, person2) === true)
    assert(socialNetwork.friendOfFriend(person1, person6) === false)

  }

  test("isDirectConnection") {

    assert(socialNetwork.isDirectConnection(person1, person4) === true)
    assert(socialNetwork.isDirectConnection(person1, person1) === true)
    assert(socialNetwork.isDirectConnection(person1, person2) === false)
  }

  test("recommended") {

    assert(socialNetwork.recommended(person1) === Seq(person2))
    assert(socialNetwork.recommended(person2) === Seq(person5))
    assert(socialNetwork.recommended(person3) === Seq(person1, person4))
    assert(socialNetwork.recommended(person4) === Seq(person3))
    assert(socialNetwork.recommended(person5) === Seq(person2))
    assert(socialNetwork.recommended(person6) === List[Person]())

  }

  test("recommendedWithCount") {

    assert(socialNetwork.recommendedWithCount(person1) === ListMap[Person,Int](person2->1))
    assert(socialNetwork.recommendedWithCount(person2) === ListMap[Person,Int](person5->1))
    assert(socialNetwork.recommendedWithCount(person3) === ListMap[Person,Int]((person1->1),(person4->1)))
    assert(socialNetwork.recommendedWithCount(person4) === ListMap[Person,Int](person3->2))
    assert(socialNetwork.recommendedWithCount(person5) === ListMap[Person,Int](person2->1))
    assert(socialNetwork.recommendedWithCount(person6) === ListMap[Person,Int]())

  }

  test("recommendationGraph") {

    val actualRecommendationGraph = socialNetwork.recommendationGraph
    val expectedRecommendationGraph = Map(person1->List(person2), person2-> List(person5),
    person3->List(person1,person4),person4->List(person3),person5->List(person2)
      ,person6->List[Person]())
    assert(actualRecommendationGraph===expectedRecommendationGraph)

  }

  test("recommendationGraphWithStats") {

    val actualRecommendationGraphWithStats = socialNetwork.recommendationGraphWithStats

    val expectedRecommendationGraphWithStats = Map[Person,ListMap[Person,Int]](
    (person1->ListMap[Person,Int](person2->1)), (person5->ListMap[Person,Int](person2->1)),
     (person2-> ListMap[Person,Int](person5->1)), (person6->ListMap[Person,Int]()),
     (person4->ListMap[Person,Int](person3->2)), (person3->ListMap[Person,Int]((person1->1),(person4->1))))

    assert(actualRecommendationGraphWithStats===expectedRecommendationGraphWithStats)

  }

  test("similarUsers") {

    val actualClusters = socialNetwork.similarUsers
    val expectedClusters = Map[String,List[Person]](
      ("Alberta"->List(person1,person4)),("Vancouver"->List(person5)),
      ("Winnipeg"->List(person2)),("Toronto"->List(person6,person3)))
    assert(actualClusters===expectedClusters)

  }


}
