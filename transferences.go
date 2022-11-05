package main

import (
	"context"
	"log"
	"net/http"
	"time"

	"github.com/julienschmidt/httprouter"
	"go.mongodb.org/mongo-driver/bson"

	"xvitu/transferences/bounderies"
	xvitu_mongo "xvitu/transferences/infra"
)

func main() {
	router := httprouter.New()

	client := xvitu_mongo.CreateClient()

	collection := client.Database("transferences").Collection("transferences")

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	res, err := collection.InsertOne(ctx, bson.D{{"desdiny", "123"}, {"origin", "abc"}, {"value", 200.00}})

	if err != nil {
		panic(err)
	}

	print(res)

	router.POST("/transference", bounderies.Transfer)

	log.Fatal(http.ListenAndServe(":8080", router))

}
