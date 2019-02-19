function down(category,fileName) {
	var data = {
		category : category,
		fileName : fileName	
	};
	if (data != null) {
		$.ajax({
			type : 'POST',
			url : '/filedown',
			datatype : 'json',
			contentType : 'application/json; charset=utf-8',
			data : JSON.stringify(data)
		});
	};
};
