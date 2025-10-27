		document.getElementById("searchBtn").onclick = function () {
		    let searchType = document.getElementsByName("searchType")[0].value;
		    let keyword = document.getElementsByName("keyword")[0].value;

		    let url = "/board-test/board/search.do?searchType=" + searchType + "&keyword=" + keyword ;
		    location.href = encodeURI(url);
		};
		