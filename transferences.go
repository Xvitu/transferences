package main

import (
	"log"
	"net/http"

	"github.com/julienschmidt/httprouter"

	"xvitu/transferences/bounderies"
)

func main() {
	router := httprouter.New()

	router.POST("/transference", bounderies.Transfer)

	log.Fatal(http.ListenAndServe(":8080", router))
}
