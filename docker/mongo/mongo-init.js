db = db.getSiblingDB("transferences");


db.createUser(
    {
        user: "transferences",
        pwd: "strong_password",
        roles: [
            {
                role: "readWrite",
                db: "transferences"
            }
        ]
    }
)


db.createCollection("transferences")
