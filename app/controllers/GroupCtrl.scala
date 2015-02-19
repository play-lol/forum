package controllers

import play.api._
import play.api.mvc._

import models._
import Group.Groups
import models.Post.Posts

import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current

import scala.slick.jdbc.meta.MTable

object GroupCtrl extends Controller {

  DB.withSession { implicit rs =>
    if (MTable.getTables(Groups.baseTableRow.tableName).list.isEmpty) {
      Groups.ddl.create
      Posts.ddl.create
      println("create table")
    }

    println(Posts.baseTableRow.tableName)
  }

  def index = DBAction { implicit rs =>
    val content = views.html.group.group(Groups.list)
    Ok(views.html.main("Group")(content))
  }

  def new_ = Action {
    val content = views.html.group.new_(Group.groupForm)
    Ok(views.html.main("New Group")(content))
  }

  def create = DBAction { implicit rs =>
    Group.groupForm.bindFromRequest.fold(
      error => {
        BadRequest(error.errorsAsJson)
      },
      group => {
        val gid = (Groups returning Groups.map(_.id)) += group
        Redirect(routes.GroupCtrl.show(gid))
      })
  }

  def edit(id: Int) = DBAction { implicit rs =>
    Groups.filter(_.id === id).firstOption match {
      case Some(g) =>
        val form = Group.groupForm.fill(g)
        val content = views.html.group.edit(id, form)
        Ok(views.html.main("Edit")(content))
      case None =>
        BadRequest("id not found " + id)
    }
  }

  def show(id: Int) = DBAction { implicit rs =>
    val posts = (for (g <- Groups if (g.id === id); 
          p <- Posts if g.id === p.groupId) yield (g, p)).list.map(_._2)
    
    Groups.filter(_.id === id).firstOption match {
      case Some(g) =>
        val content = views.html.group.show(g, posts)
        Ok(views.html.main(g.title)(content))
      case None =>
        BadRequest("id not found " + id)
    }
  }
  
  def update(id: Int) = DBAction { implicit rs =>
    Group.groupForm.bindFromRequest.fold(
      error => {
        BadRequest(error.errorsAsJson)
      },
      group => {
        val updateGroup = group.copy(id = id)
        Groups.filter(_.id === id).update(updateGroup)
        Redirect(routes.GroupCtrl.index())
      })
  }

  def destroy(id: Int) = DBAction { implicit rs =>
    Groups.filter(_.id === id).delete
    Redirect(routes.GroupCtrl.index)
  }
}
