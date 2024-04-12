(ns recipe-website.controller
  (:require [reitit.ring :as ring]
            [recipe-website.views :as views]
            [ring.util.http-response :as response]
            [recipe-website.model :as model]))

(defn html-response [content]
  (-> (str content)
      response/ok
      (response/content-type "text/html")))

(defn home-controller [req]
  (html-response (views/home-page)))

(defn recipe-controller [req]
  (html-response
   (views/recipe-page
    (model/get-recipe (:recipe-id (get-in req [:path-params :id]))))))

(defn creater-handler [db]
  (ring/ring-handler
   (ring/router
    [["/" {:handler home-controller}]
     ["/recipe/:id" {:get {:parameters {:path {:id int?}}}
                     :handler recipe-controller }]]
    {:data {:db db}})))
