package com.example.vetJa.models.user

//{
//    "signIn": {
//    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY0NzAxMWYyLTI4ZDAtNGI1MS1hYWYyLTEyOGVlMDU0YjlkMCIsImlhdCI6MTc0ODYzMjI2NiwiZXhwIjoxNzQ4NzE4NjY2fQ.Tg0L6j3UaKwyo0eZX0Nw_NuxovMITsKnYWePFsNkIIE",
//    "user": {
//    "nome": "Nicolas",
//    "email": "nicolas1@gmail.com",
//    "idCliente": "647011f2-28d0-4b51-aaf2-128ee054b9d0"
//}
//}
//}

data class UserResponse(
    val signIn: SignIn
)

data class SignIn(
    val token: String,
    val user: UserReply
)

data class UserReply(
    val id: String,
    val nome: String,
    val email: String,
    val idCliente: String
)