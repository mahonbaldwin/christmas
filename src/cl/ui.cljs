(ns cl.ui
  (:require
    [cljs-time.core :as time]
    [rum.core :as rum :refer-macros [defc]]))

(def offset (atom 0))

(def year (.getYear (time/now)))

(defn crazy-finger-message [o]
  (condp < o
    4000 "What exactly do you expect to see at this point?"
    3000 "Is the earth even going to be around at this point?"
    2000 "...two medals!"
    1000 "You deserve a medal!"
    350 "Wow! You are REALLY presistant."
    222 "Are you seriously still at this?"
    122 "Okay, fine. Have it your way."
    121 "I can see I'm not getting through to you."
    120 "Are you done yet?"
    82 "We got to stop meeting like this."
    45 "You know there's no extra credit...right?"
    37 "Seriously?"
    nil))

(def people ["Michael" "Savannah" "Taylor" "Drew" "Cole"])

(defn assign-people
  "Takes list people and a display-year and returns vector of vectors with [givers recievers]"
  [people display-year]
  (let [display-year (+ display-year 1)                     ; sync up with where we are in the rotation
        offset       (+ (mod display-year (- (count people) 1)) 1)
        recievers    (into (subvec people offset) (subvec people 0 offset))]
    (partition 2 (interleave people recievers))))

(defc root < rum/reactive []
  [:div
   [:div.content
    [:h1 "Christmas " (+ year (rum/react offset))]
    (when (crazy-finger-message (rum/react offset)) [:div (crazy-finger-message (rum/react offset))])
    [:div.recipients
     (for [p (assign-people people (+ year (rum/react offset)))]
       [:p {:key (str "gifter-" p)}
        (first p) " has " (last p) "."])]
    [:.nav
     (if (< 0 (rum/react offset)) [:span.link {:on-click #(swap! offset dec)} "← Previous Year"] [:span])
     [:span.link {:on-click #(reset! offset 0)} "Reset"]
     [:span.link {:on-click #(swap! offset inc)} "Next Year →"]]]
   [:.disclaimer "* If you notice errors on this page, please write a detailed note on a $20 bill and send it to Mahon."]])

(defn mount []
  (rum/mount (root) (.getElementById js/document "app")))