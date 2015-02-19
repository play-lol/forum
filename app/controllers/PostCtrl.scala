package controllers

import play.api._
import play.api.mvc._

import models._
import models.Group.Groups
import models.Post.Posts

import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current

import scala.slick.jdbc.meta.MTable

object PostCtrl extends Controller {
  DB.withSession { implicit rs =>
    if (MTable.getTables(Posts.baseTableRow.tableName).list.isEmpty)
      Posts.ddl.create
  }

  def new_(gid: Int) = DBAction { implicit rs =>
    Groups.filter(_.id === gid).firstOption match {
      case Some(g) =>
        val content = views.html.post.new_(g, Post.postForm)
        Ok(views.html.main("New Post")(content))
      case None =>
        BadRequest("gid not found " + gid)
    }
  }

  def create(gid: Int) = DBAction { implicit rs =>
    Groups.filter(_.id === gid).firstOption match {
      case Some(g) =>
        Post.postForm.bindFromRequest.fold(
          error => {
            BadRequest(error.errorsAsJson)
          },
          post => {
            val newPost = post.copy(groupId = gid)
            Posts += newPost
            Redirect(routes.GroupCtrl.show(g.id))
          }
        )
      case None =>
        BadRequest("gid not found " + gid)
    }
  }

  def edit(gid: Int, id: Int) = DBAction { implicit rs =>
    (for (g <- Groups if (g.id === gid);
          p <- Posts if (p.id === id)) 
      yield (g, p)).firstOption match {
      case Some((g, p)) =>
        val form = Post.postForm.fill(p)
        val content = views.html.post.edit(g, p, form)
        Ok(views.html.main("Edit")(content))
      case None =>
        BadRequest(s"id not found groupId=$gid, postId=$id")
    }
  }

  def update(gid: Int, id: Int) = DBAction { implicit rs =>
    Post.postForm.bindFromRequest.fold(
      error => {
        BadRequest(error.errorsAsJson)
      },
      group => {
        val updateGroup = group.copy(id = id, groupId = gid)
        Posts.filter(p => p.groupId === gid && p.id === id).update(updateGroup)
        Redirect(routes.GroupCtrl.show(gid))
      })
  }

  def destroy(gid: Int, id: Int) = DBAction { implicit rs =>
    Posts.filter(p => p.groupId === gid && p.id === id).delete
    Redirect(routes.GroupCtrl.show(gid))
  }
}
