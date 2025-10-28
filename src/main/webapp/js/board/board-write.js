(function () {
  const fileList = document.getElementById('file-list');
  const addBtn = document.getElementById('btnAddFile');
  const maxFiles = 5;
  let count = (fileList && fileList.querySelectorAll('.file-group').length) || 0;

  

  if (addBtn && fileList) {
    addBtn.addEventListener('click', function () {
      if (count >= maxFiles) {
        alert(`파일은 최대 ${maxFiles}개까지 업로드할 수 있습니다.`);
        return;
      }

      const html = `
        <div class="file-group">
          <input class="form-control" type="file" name="file" />
          <button type="button" class="btn btn-delete-file">삭제</button>
        </div>
      `;
      fileList.insertAdjacentHTML('beforeend', html);
      count++;
    });
  }

  if (fileList) {
    fileList.addEventListener('click', function (e) {
      const target = e.target;
      if (!target) return;

      if (target.classList.contains('btn-delete-file')) {
        e.preventDefault();
        const group = target.closest('.file-group');
        if (group) group.remove();
        if (count > 0) count--;
      }
    });
  }
})();
