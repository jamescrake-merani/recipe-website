(ns recipe-website.controller
  (:require [reitit.ring :as ring]
            [recipe-website.views :as views]
            [ring.util.http-response :as response]))

(defn html-response [content]
  (-> (str content)
      response/ok
      (response/content-type "text/html")))

(defn home-controller [req]
  (html-response (views/home-page)))

(defn handler [db req]
  (ring/ring-handler
   (ring/router
    [["/" {:get home-controller}]])
   {:data {:db db}}))

