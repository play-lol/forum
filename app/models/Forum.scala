package models

import play.api.data._
import play.api.data.Forms._

import play.api.db.slick.Config.driver.simple._

case class Group(id: Int, title: String, description: String)

class GroupTable(tag: Tag) extends Table[Group](tag, "Group") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title", O.NotNull)
  def description = column[String]("description")
  def * = (id, title, description) <> ((Group.apply _).tupled, Group.unapply)
}

object Group {
  val groupForm = Form(
    mapping(
      "Title" -> nonEmptyText,
      "Description" -> text
    )((title, desc) => Group(-1, title, desc))(g => Some(g.title, g.description)))

  val Groups = TableQuery[GroupTable]
}

case class Post(id: Int, groupId: Int, content: String)

class PostTable(tag: Tag) extends Table[Post](tag, "Post") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def groupId = column[Int]("groupId", O.NotNull)
  def content = column[String]("content", O.NotNull)
  def * = (id, groupId, content) <> ((Post.apply _).tupled, Post.unapply)
}

object Post {
  val postForm = Form(mapping(
      "content" -> text
    )((content) => Post(-1, -1, content))
     (p => Some(p.content))
  )
  
  val Posts = TableQuery[PostTable]
}

