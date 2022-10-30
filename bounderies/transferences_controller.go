package bounderies

import (
	"fmt"
	"net/http"

	"xvitu/transferences/utils/request"

	"github.com/julienschmidt/httprouter"
)

func Transfer(w http.ResponseWriter, r *http.Request, ps httprouter.Params) {
	var transferenceRequest TransferenceRequest

	request.DecodeJsonRequest(r, &transferenceRequest)

	fmt.Printf("\n\n json object:: %+v", transferenceRequest)
}

type TransferenceRequest struct {
	Origin  string  `json:"origin"`
	Destiny string  `json:"destiny"`
	Value   float64 `json:"value"`
}
