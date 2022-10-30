package request

import (
	"encoding/json"
	"net/http"
)

func DecodeJsonRequest(request *http.Request, dto any) {
	decoder := json.NewDecoder(request.Body)

	err := decoder.Decode(&dto)

	if err != nil {
		panic(err)
	}
}
