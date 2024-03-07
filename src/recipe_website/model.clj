(ns recipe-website.model
  (:require [honey.sql :as sql]))

(def recipe-table
  (sql/format
   {:create-table :recipe
    :with-columns [[:id :primary-key :auto-increment]
                   [:name :text]
                   [:time-guidance :int]
                   [:published :boolean]]}))

(def tag
  (sql/format
   {:create-table :tag
    :with-columns [[:recipe-id :int :foreign-key [:references :recipe :id]]
                   [:name :text]
                   [[:primary-key [:recipe-id :name]]]]}))
