(ns recipe-website.model
  (:require [honey.sql :as sql]
            [next.jdbc :as jdbc]))

(def recipe-table
  (sql/format
   {:create-table :recipe
    :with-columns [[:id :primary-key]
                   [:name :text]
                   [:time-guidance :int]
                   [:published :boolean]]}))

(def tag
  (sql/format
   {:create-table :tag
    :with-columns [[:recipe-id :int :references [:recipe :id]]
                   [:name :text]
                   [[:primary-key :recipe-id :name]]]}))

(def recipe-line
  (sql/format
   {:create-table :recipe-line
    :with-columns [[:recipe-id :int :references [:recipe :id]]
                   [:line-order :int]
                   [:content :text]
                   [[:primary-key :recipe-id :order]]]}))

(defn create-tables [db]
  (jdbc/execute-one! db recipe-table)
  (jdbc/execute-one! db tag)
  (jdbc/execute-one! db recipe-line))
