console.log("this is me vishal karape");

const togglesidebar = () => {
	
	if($(".sidebar").is(":visible")){
		//true
		//band karo
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
		
		
	}else{
		//false
		//show karna hai
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	};
};