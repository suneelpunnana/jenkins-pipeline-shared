import groovy.json.*

def call(jsondata,bitbucket,bamboo,sonar){
def jsonString = jsondata
def jsonObj = readJSON text: jsonString
int ecount = jsonObj.config.emails.email.size()
	def team=jsonObj.riglet_info.name
List<String> jsonStringa= new ArrayList<String>();
  jsonStringa.add(bitbucket)
   jsonStringa.add(bamboo)
	jsonStringa.add(sonar)
   //jsonStringa.add(gitlab)
  //println(jsonStringa)
/*def jsonStringa = bitbucket
def jsonObja = readJSON text: jsonStringa
  
def jsonStringb = bamboo
def jsonObjb = readJSON text: jsonStringb*/
 int[] score1= new int[100]
 List<String> JSON = new ArrayList<String>();
  List<String> LIST = new ArrayList<String>();
  List<String> JSON1 = new ArrayList<String>();
	
for(i=0;i<jsonStringa.size();i++)
  { 
    int score=0
    String name="  "
	  String metric=" "
if(jsonStringa[i].contains("bitbucket"))
    {
      name="bitbucket"
	  //  metric="commits"
//def jsonStringa = bitbucket
def jsonObja = readJSON text: jsonStringa[i]
int total=jsonObja.bitbucket.Commit_count
 // println(jsonObja)
  //println(total)
 
	    LIST.add(["toolName":name,"metric":"commits","value":total])
 /* if(total>5)
  {
    score=score+10
  }*/
  }
   if(jsonStringa[i].contains("Bamboo"))
    {
      name="bamboo"
	   // metric="successfulbuilds"
    //  def jsonStringb = bamboo
def jsonObjb = readJSON text: jsonStringa[i]
  //println(jsonObj)
def total=jsonObjb.Bamboo.totalBuilds
  def scnt =jsonObjb.Bamboo.teamsuccessbuild_cnt
	    def fcnt=jsonObjb.Bamboo.teamfailurebuild_cnt
      
 // def res=bamboo1.bamboo.teamsuccessbuild_cnt
 // def obj = JSON.parse(bamboo1)
 //println(cnt)
	  
	    LIST.add(["toolName":name,"metric":"total_builds","value":total])
	    
 
	    LIST.add(["toolName":name,"metric":"success_builds","value":scnt])
	    

	    LIST.add(["toolName":name,"metric":"failure_builds","value":fcnt])
	    
    }
	  
    if(jsonStringa[i].contains("Sonar"))
    {
	    name="Sonar"
	    def jsonObjc = readJSON text: jsonStringa[i]
	    for(i=0;i<jsonObjc.Sonar.Metrics.component.measures.size();i++){
		    print jsonObjc.Sonar.Metrics.component.measures
    def sonar_metric=jsonObjc.Sonar.Metrics.component.measures[i].metric
		    def d=jsonObjc.Sonar.Metrics.component.measures[i].value
    double data = Double.parseDouble(d); 
       LIST.add(["toolName":name,"metric":sonar_metric,"value":data])
	    }
    }
	   // metric="successfulbuilds"
    //  def jsonStringb = bamboo

  //println(jsonObj)


	 
      
 // def res=bamboo1.bamboo.teamsuccessbuild_cnt
 // def obj = JSON.parse(bamboo1)
 //println(cnt)
	   
	  


	
	 

	
	


	  //JSON1[i]=LIST.clone()
    //println(score)
   //JSON.add(JSON1[i])  
	  //LIST.clear()
  }
def jsonBuilder = new groovy.json.JsonBuilder()

jsonBuilder(
 "teamName":team,
  "metrics" : LIST
  
) 
  
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/Teamscore.json")
file.write(jsonBuilder.toPrettyString())	
  
 
  //println(JSON)
}

