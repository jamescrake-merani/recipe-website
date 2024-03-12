(ns recipe-website.model
  (:require [honey.sql :as sql]
            [next.jdbc :as jdbc]))

(comment
  (def test-data-source (jdbc/get-datasource {:dbtype "sqlite" :dbname "test.db"})))

(def recipe-table
  (sql/format
   {:create-table [:recipe :if-not-exits]
    :with-columns [[:id :primary-key]
                   [:name :text]
                   [:time-guidance :int]
                   [:published :boolean]]}))

(def tag
  (sql/format
   {:create-table [:tag :if-not-exists]
    :with-columns [[:recipe-id :int :references [:recipe :id]]
                   [:name :text]
                   [[:primary-key :recipe-id :name]]]}))

(def recipe-line
  (sql/format
   {:create-table [:recipe-line :if-not-exits]
    :with-columns [[:recipe-id :int :references [:recipe :id]]
                   [:line-order :int]
                   [:content :text]
                   [[:primary-key :recipe-id :line-order]]]}))


(defn create-tables [db]
  (map #(jdbc/execute-one! db %)
       [recipe-table tag recipe-line]))
