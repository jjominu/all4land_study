(function () {
  var backBtn = document.getElementById('btnBack');
  if (backBtn) {
    backBtn.addEventListener('click', function () {
      window.history.back();
    });
  }

  var deleteForm = document.getElementById('deleteForm');
  if (deleteForm) {
    var deleting = false;
    deleteForm.addEventListener('submit', function (e) {
      if (deleting) {
        e.preventDefault();
        return;
      }
      if (!confirm('이 게시글을 삭제하시겠습니까?')) {
        e.preventDefault();
        return;
      }
      deleting = true;
    });
  }

  document.addEventListener('click', function (e) {
    var a = e.target.closest('a.bd-download');
    if (!a) return;
    // 예: 특정 확장자 경고하고 싶다면 여기서 처리
    // var name = a.getAttribute('data-filename') || '';
  });

  var container = document.querySelector('.container');
  if (container) {
    container.addEventListener('click', function (e) {
      var img = e.target.closest('img.bd-img');
      if (!img) return;
      window.open(img.src, '_blank');
    });
  }
})();
