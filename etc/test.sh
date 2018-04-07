#!/usr/bin/env bash
curl --header "Content-Type: application/soap+xml;charset=UTF-8" \
 --header "SOAPAction:urn:findMe" \
 --data @request.xml http://localhost:8080/findMe
