import groovy.json.*
import groovy.json.JsonOutput
def call(jsondata,bamboo,bitbucket,gitlab)
{
def jsonString = jsondata
def jsonObj = readJSON text: jsonString
int ecount = jsonObj.config.emails.email.size()
	def team=jsonObj.riglet_info.name

 
 List<String> JSON = new ArrayList<String>();
  List<String> LIST = new ArrayList<String>();
  List<String> JSON1 = new ArrayList<String>();
	List<String> jsonStringa= new ArrayList<String>();
	jsonStringa.add(bamboo)
  jsonStringa.add(bitbucket)
   jsonStringa.add(gitlab)
	 for(j=0;j<ecount;j++)
   {
	 def email=jsonObj.config.emails.email[j] 
	   int score=0
    int reward=0
    String name="  "
	 for(i=0;i<jsonStringa.size();i++)
  { 
   
	  
  if(jsonStringa[i].contains("Bamboo"))
    {
 
	   
	    
     name="bamboo"
    //  def jsonStringb = bamboo
	   // def jsonString1 = jsonStringa[i]
	   def jsonObja = readJSON text: jsonStringa[i]

  //println(jsonObja)
  def scnt =jsonObja.Bamboo.individualsuccess[j].Success_cnt
  def fcnt =jsonObja.Bamboo.individualfailure[j].Failure_cnt
 def email1=jsonObja.Bamboo.individualsuccess[j].email
      
 // def res=bamboo1.bamboo.teamsuccessbuild_cnt
 // def obj = JSON.parse(bamboo1)
 println(scnt)
 //int score=0
	  
 if(email==email1 && scnt>1)
  {
   score=scnt*1
    LIST.add(["toolname":name,"metric":"No of more successful builds","score":score])
	reward=reward+score  
    score=0
  }
     if(email==email1 && fcnt<2 && scnt!=0)
  {
   score=score+10 
    LIST.add(["toolname":name,"metric":"No of least failure builds","score":score])
	  reward=reward+score 
    score=0
  }
   }
	    if(jsonStringa[i].contains("bitbucket"))
    {
      name="bitbucket"
	    
	    
//def jsonStringa = bitbucket
def jsonObjb = readJSON text: jsonStringa[i]
int total=jsonObjb.bitbucket.Individual_commits[j].Commit_count
	    def email1=jsonObjb.bitbucket.Individual_commits[j].Email
 // println(jsonObja)
  //println(total)
 
  if(email==email1 && total>1)
  {
    score=total*1
	   LIST.add(["toolname":name,"metric":"commits","score":score])
	  reward=reward+score
	  score=0
  }
  }
	  if(jsonStringa[i].contains("gitlab"))
      {
        name="gitlab"
	      //metric="commits"
        def jsonObjc= readJSON text: jsonStringa[i]
  //println(jsonObj)
  def cnt =jsonObjc.gitlab.individual_commit_Details[j].Commit_cnt
	       def email1=jsonObjc.gitlab.individual_commit_Details[j].email
	       if(email==email1 && cnt>1)
  {
    score=cnt*1
	   LIST.add(["toolname":name,"metric":"commits","score":score])
	  reward=reward+score
	  score=0
  }
      }
    }
	   JSON1[j]=LIST.clone()
	   
   JSON.add(["email":email,"reward": reward,"metrics":JSON1[j]])
    LIST.clear()
	reward=0    
	   
    }
	def me=0
	for(i=0;i<JSON.size();i++)
	{
for(j=i;j<JSON.size();j++)
		{
			
			
			if(JSON[i].reward<=JSON[j].reward )
						{
							me=JSON[i]
							JSON[i]=JSON[j]
							JSON[j]=me
						}
						}
						}
     def jsonBuilder = new groovy.json.JsonBuilder()

jsonBuilder(
 "teamName":team,
  "metrics" : JSON
  
) 
  
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/game.json")
file.write(jsonBuilder.toPrettyString())
    
  //println(JSON)
}
