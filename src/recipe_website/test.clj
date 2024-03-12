(ns recipe-website.test
  (:require  [recipe-website.model :as model]))


(def test-recipe
  {:name "Cement"
   :time-guidance 60
   :published true
   :lines ["Purchase or collect limestone"
           "Break the limestone into small pieces"
           "Cook the limestone in a kiln or outdoor even"
           "Let he baked limestone cool"
           "Crumble the baked limestone chunks"]})


(defn populate-database [db]
  (model/create-tables db)
  (model/insert-recipe db test-recipe))
