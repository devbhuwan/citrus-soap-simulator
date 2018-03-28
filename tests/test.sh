#!/usr/bin/env bash
curl --header "Content-Type: text/xml;charset=UTF-8" \
 --header "SOAPAction:urn:getTodo" \
 --data @request.xml http://localhost:8080/getTodo
