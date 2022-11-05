package mongo

import (
	"context"
	"time"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

func CreateClient() *mongo.Client {
	ctx, cancel := context.WithTimeout(context.Background(), 60*time.Second)
	defer cancel()
	client, err := mongo.Connect(ctx, options.Client().ApplyURI("mongodb://transferences:strong_password@localhost:27017/transferences"))

	if err != nil {
		panic(err)
	}

	return client

	// defer func() { _ = client.Disconnect(ctx) }()
}
