(ns recipe-website.model
  (:require [honey.sql :as sql]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as jsql]))

(def recipe-table
  (sql/format
   {:create-table [:recipe :if-not-exists]
    :with-columns [[:id :integer :primary-key]
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
   {:create-table [:recipe-line :if-not-exists]
    :with-columns [[:recipe-id :int :references [:recipe :id]]
                   [:line-order :int]
                   [:content :text]
                   [[:primary-key :recipe-id :line-order]]]}))

;; I don't really know if ordering of the ingredients on a recipe is a detail
;; we're bothered about but I think at least give the user the freedom to
;; control it if it really does matter to them.

(def ingredient
  (sql/format
   {:create-table [:ingredient :if-not-exists]
    :with-columns [[:recipe-id :int :references [:recipe :id]]
                   [:ingredient-order :int]
                   [:ingredient-name :text]
                   [:quantity :real]
                   [:measurement :text]
                   [[:primary-key :recipe-id :ingredient-order]]]}))

(defn get-recipe [db recipe-id]
  (into
   {:lines (jdbc/execute!
            db
            (sql/format
             {:select :* :from :recipe-line :where [:= :recipe-id recipe-id]}))
    :ingedients (jdbc/execute!
                 db
                 (sql/format
                  {:select :* :from :ingredient :where [:= :recipe-id recipe-id]}))}
   (jdbc/execute-one!
    db
    (sql/format
     {:select :* :from :recipe :where [:= :id recipe-id]}))))

(defn insert-recipe [recipe db]
  "RECIPE is represented as a map with the following keys
:name
:time-guidance
:published
:lines (vector of line strings assumed to be in order).

:ingredients (vector of maps assumed to be in order with the following)

:name
:quantity (number)
:measurement (str)
"
  (let [recipe-id
        ;; Insert the recipe itself
        (-> (jdbc/execute-one!
             db
             (sql/format
              {:insert-into :recipe
               :columns [:name :time-guidance :published]
               :values [[(:name recipe) (:time-guidance recipe) (:published recipe)]]})
             {:return-keys true})
            (vals)
            (first))
        lines (:lines recipe)
        ingredients (:ingredients recipe)
        lines-range (range (count lines))
        ingredients-range (range (count ingredients))]
    ;; Insert the recipe lines.
    (jdbc/execute-one!
     db
     (sql/format
      {:insert-into :recipe-line
       :columns [:recipe-id :line-order :content]
       :values (map (fn [line order]
                      [recipe-id order line])
                    lines lines-range)}))
    ;;Finally, insert the ingredients
    (jdbc/execute-one!
     db
     (sql/format
      {:insert-into :ingredient
       :columns [:recipe-id :ingredient-order :ingredient-name :quantity :measurement]
       :values (map (fn [order ingredient]
                      [recipe-id
                       order
                       (:name ingredient)
                       (:quantity ingredient)
                       (:measurement measurement)])
                    ingredients ingredients-range)}))))

(defn create-tables [db]
  (doseq [table [recipe-table tag recipe-line]]
    (jdbc/execute-one! db table)))
