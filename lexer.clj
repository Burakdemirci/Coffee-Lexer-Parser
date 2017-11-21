(ns lexer (:import [java.util ArrayList])  )

; Burak DEMİRCİ
; 141044091
; burakdemirci@gtu.edu.tr

(def operators (new String ")(*+-/"))
(def Keywords (new ArrayList ))
(def TokenList (new ArrayList))

; Dosyadan okuma
(defn read_file [filename]
  (slurp filename)
) 

; Bazı tanımlamaları yapma
(defn initData []
  (.add Keywords "and")
  (.add Keywords "or")
  (.add Keywords "not")
  (.add Keywords "equal")
  (.add Keywords "append")
  (.add Keywords "concat")
  (.add Keywords "set")
  (.add Keywords "deffun")
  (.add Keywords "for")
  (.add Keywords "while")
  (.add Keywords "if")
  (.add Keywords "then")
  (.add Keywords "else")
  (.add Keywords "true")
  (.add Keywords "false")
)

(defn operatorCheck [val] ; Verilen degerin Operator olup olmadıgı kontrol ediyor
   (def ret nil)

   (loop [x 0 ]
     (when (< x (count operators))
       (if (=  (nth operators x) val)
          (do
             (def ret 1)
          )   
       )
      (recur ( + x 1))))
   (boolean ret)
)

(defn KeywordCheck [value] ; Verilen degerin Keyword olup olmadıgı kontrol ediyor
  (def ret nil)
  (loop [x 0]
    (when (< x (count Keywords))
      
      (if (= (.get Keywords x) value)
        (def ret 1)  
       )
       (recur ( + x 1))
      )
    )
  (boolean ret)
)

(defn isNum [value] ; Verilen degerin(Constand) Integer olup olmadıgı kontrol ediyor
   (number? (read-string value))  
) 

(defn Helper [value] ; Keyword , Integer ve Identifier 
   (if (isNum value)
       (.add TokenList (.concat "Constant_" (.toString value)))
       (do
         (if (KeywordCheck value)
             (.add TokenList (.concat "Keywords_" (.toString value)))
             (.add TokenList (.concat "Identifier_" (.toString value)))
         )
       )  
   )      
)

; Main 
(defn lexer [filename]
   (initData)
   (def data(read_file filename))
   (def flag 0)
   (def buffer "")
   (def ReturnList '())
   (loop [x 0]
      (when (< x (count data))
        
         (if (operatorCheck (nth data x)) ; Operator ise
             (do ;******************************
               (if (= (count buffer) 0)
                 (.add TokenList (.concat "Operator_" (.toString (nth data x))))
               )
              (def flag 1)
             ) ;*******************************
             (do ; Else Start
                
               (if (and (or (= (.toString (nth data x)) " " ) (= (.toString (nth data x)) "\n" )) (> (count buffer) 0))
                (do 
                  (Helper buffer)
                  (def buffer "")
                )
                (do ; 2nd Else part 
                   (if  (not (or (or (= (int (nth data x)) 32 ) (= (int (nth data x)) 13 )) (= (int (nth data x)) 10 ) )) 
                    (do
                       (def buffer (str buffer (.toString (nth data x))))
                    )
                   )
                ))
                (def flag 0) 
             ) ; Else Close   
         )
       
         (if (and (= flag 1)(> (count buffer) 0))
           (do
              (Helper buffer)
              (.add TokenList (.concat "Operator_" (.toString (nth data x))))
              (def falag 0)
              (def buffer "")
           )
         )
      (recur (+ x 1)))
   )

   (loop [x (-(count TokenList)1)] 
     (when (> x -1)
        (def ReturnList (conj ReturnList (.get TokenList x)))
     (recur (- x 1))  
     )
   )
   (seq ReturnList )
)

; Call the lexer 
(lexer "CoffeeSample.coffee")

 