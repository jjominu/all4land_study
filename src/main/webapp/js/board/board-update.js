(function () {
  const fileList = document.getElementById('file-list');
  const addBtn = document.getElementById('btnAddFile');
  const deleteBucket = document.getElementById('delete-bucket') || document.getElementById('boardForm');
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

      if (target.classList.contains('btn-delete-existing')) {
        e.preventDefault();
        const fileId = target.getAttribute('data-file-id');
        if (fileId && deleteBucket) {
          const hidden = document.createElement('input');
          hidden.type = 'hidden';
          hidden.name = 'deleteFileIds';
          hidden.value = fileId;
          deleteBucket.appendChild(hidden);
        }
        const container = target.closest('p') || target.closest('.attached-file');
        if (container) container.remove();
        return;
      }

      if (target.classList.contains('btn-delete-file')) {
        e.preventDefault();
        const group = target.closest('.file-group');
        if (group) group.remove();
        if (count > 0) count--;
      }
    });
  }
})();
