(ns recipe-website.test
  (:require  [recipe-website.model :as model]
             [next.jdbc :as jdbc]))

(def test-recipe
  {:name "Cement"
   :time-guidance 60
   :published true
   :lines ["Purchase or collect limestone"
           "Break the limestone into small pieces"
           "Cook the limestone in a kiln or outdoor even"
           "Let he baked limestone cool"
           "Crumble the baked limestone chunks"]
   :ingredients [{:name "Limestone" :quantity 100 :measurement "grams"}
                 {:name "Something Else" :quantity 323 :measurement "tests"}
                 {:name "Sanity" :quantity 1 :measurement "idk anymores"}]})

(defn populate-database [db]
  (model/create-tables db)
  (model/insert-recipe test-recipe db ))

(defn create-and-populate-database []
  (let [new-db (jdbc/get-connection {:dbtype "sqlite" :dbname ":memory:"})]
    (populate-database new-db)
    new-db))
