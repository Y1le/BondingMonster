后端 API 文档
项目概述

本项目是一个基于Spring Boot、MyBatis、Vue.js和MySQL的全栈应用。后端提供RESTful API，前端通过Vue.js与后端进行交互。用户认证采用JWT（JSON Web Token）机制，实现安全的登录和资源访问。

技术栈

后端: Spring Boot, MyBatis, MySQL, JWT
前端: Vue.js, Vuex, jQuery, jwt-decode
API 文档

基础URL: https://localhost:8080/

认证方式:

所有需要认证的API请求都应在请求头中包含 Authorization: Bearer <access_token>。

1. 用户认证与授权
1.1 用户登录
URL: /api/token/
方法: POST
描述: 用户通过用户名和密码进行登录，成功后获取访问令牌（Access Token）和刷新令牌（Refresh Token）。
请求参数 (Request Body - application/x-www-form-urlencoded):
参数名	类型	必填	描述	示例
username	String	是	用户名	testuser
password	String	是	用户密码	password123
成功响应 (Status: 200 OK):
json
{
  "access": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", // JWT访问令牌
  "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." // JWT刷新令牌
}
错误响应 (Status: 400 Bad Request 或 401 Unauthorized):
json
{
  "error": "Invalid credentials" // 或其他错误信息
}
1.2 刷新访问令牌
URL: /api/token/refresh/
方法: POST
描述: 使用刷新令牌获取新的访问令牌，以延长用户会话，避免频繁登录。刷新令牌通常具有比访问令牌更长的有效期。
请求参数 (Request Body - application/x-www-form-urlencoded):
参数名	类型	必填	描述	示例
refresh	String	是	刷新令牌	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
成功响应 (Status: 200 OK):
json
{
  "access": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." // 新的JWT访问令牌
}
错误响应 (Status: 400 Bad Request 或 401 Unauthorized):
json
{
  "error": "Invalid refresh token" // 或其他错误信息
}
2. 用户信息管理
2.1 获取用户信息
URL: /myspace/getinfo/
方法: GET
描述: 获取指定用户的详细信息。
请求参数 (Query Parameters):
参数名	类型	必填	描述	示例
user_id	Integer	是	用户ID	1
请求头 (Headers):
头信息	值	描述
Authorization	Bearer <access_token>	用户的访问令牌
成功响应 (Status: 200 OK):
json
{
  "id": 1,
  "username": "mockUser",
  "photo": "https://img.shetu66.com/2023/07/27/1690436791750269.png",
  "followerCount": 100
}
错误响应 (Status: 401 Unauthorized 或 404 Not Found):
json
{
  "error": "Unauthorized" // 或 "User not found"
}