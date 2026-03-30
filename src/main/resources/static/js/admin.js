// ── SIDEBAR TOGGLE
function toggleSidebar() {
    const sb = document.getElementById('sidebar');
    const main = document.getElementById('mainContent');
    if (window.innerWidth <= 768) {
        sb.classList.toggle('open');
    } else {
        sb.classList.toggle('collapsed');
        main.classList.toggle('full');
    }
}

// ── AUTO-DISMISS ALERTS
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.alert').forEach(el => {
        setTimeout(() => { el.style.transition='opacity .4s'; el.style.opacity='0';
            setTimeout(() => el.remove(), 400); }, 4500);
    });
});

// ── CLOSE SIDEBAR ON MOBILE OUTSIDE CLICK
document.addEventListener('click', e => {
    const sb = document.getElementById('sidebar');
    const toggle = document.querySelector('.sb-toggle');
    if (window.innerWidth <= 768 && sb?.classList.contains('open')
        && !sb.contains(e.target) && !toggle?.contains(e.target)) {
        sb.classList.remove('open');
    }
});

// ── MODAL BACKDROP CLOSE
document.addEventListener('click', e => {
    if (e.target.classList.contains('modal-backdrop')) {
        e.target.classList.remove('show');
    }
});

function showModal(id) { document.getElementById(id)?.classList.add('show'); }
function hideModal(id) { document.getElementById(id)?.classList.remove('show'); }

// ── SLUG GENERATOR (Vietnamese)
function toSlug(str) {
    return str.toLowerCase()
        .replace(/[àáạảãâầấậẩẫăằắặẳẵ]/g,'a').replace(/[èéẹẻẽêềếệểễ]/g,'e')
        .replace(/[ìíịỉĩ]/g,'i').replace(/[òóọỏõôồốộổỗơờớợởỡ]/g,'o')
        .replace(/[ùúụủũưừứựửữ]/g,'u').replace(/[ỳýỵỷỹ]/g,'y').replace(/đ/g,'d')
        .replace(/[^a-z0-9\s-]/g,'').replace(/\s+/g,'-').replace(/-+/g,'-').replace(/^-|-$/g,'');
}

function bindSlugAuto(nameInputId, slugInputId) {
    const nameInput = document.getElementById(nameInputId);
    const slugInput = document.getElementById(slugInputId);
    if (!nameInput || !slugInput) return;
    nameInput.addEventListener('input', () => {
        if (!slugInput.dataset.manual) slugInput.value = toSlug(nameInput.value);
    });
    slugInput.addEventListener('input', () => { slugInput.dataset.manual = '1'; });
}

// ── IMAGE PREVIEW
function previewImage(input, previewId) {
    if (!input.files?.[0]) return;
    const reader = new FileReader();
    reader.onload = e => {
        const img = document.getElementById(previewId);
        if (img) { img.src = e.target.result; img.style.display = 'block'; }
    };
    reader.readAsDataURL(input.files[0]);
}

// ── VARIANT TABLE (product form)
function addVariantRow(sizeOptions) {
    document.getElementById('no-variant-msg')?.remove();
    const tbody = document.getElementById('variantBody');
    const opts = sizeOptions.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
    tbody.insertAdjacentHTML('beforeend', `
        <tr>
          <td><select name="vSizeId" class="form-control">${opts}</select></td>
          <td><input type="number" name="vPrice" value="0" min="0" step="1000" class="form-control"></td>
          <td><input type="text" name="vSku" placeholder="VD: TRASUA-001-M" class="form-control"></td>
          <td style="text-align:center"><input type="checkbox" name="vInStock" checked></td>
          <td><button type="button" class="btn btn-sm btn-danger" onclick="this.closest('tr').remove()">✕</button></td>
        </tr>`);
}

// ── COUPON VALUE MAX (percent max 100)
function showValueError(msg) {
    var hint = document.getElementById('couponValueHint');
    if (hint) { hint.textContent = msg; hint.style.color = '#d32f2f'; }
}

function hideValueError() {
    var hint = document.getElementById('couponValueHint');
    if (hint) { hint.textContent = 'Nhập % hoặc số tiền tùy loại'; hint.style.color = ''; }
}

function updateValueMax() {
    var type = document.getElementById('couponType');
    var input = document.getElementById('couponValue');
    if (!type || !input) return;
    if (type.value === 'percent') {
        input.max = 100;
        input.min = 0;
    } else {
        input.removeAttribute('max');
        hideValueError();
    }
}

function validateCoupon() {
    var type = document.getElementById('couponType');
    var input = document.getElementById('couponValue');
    if (type && input && type.value === 'percent') {
        var val = parseFloat(input.value);
        if (val > 100) {
            input.value = 100;
            showValueError('⚠️ Giá trị giảm không được vượt quá 100%');
            return false;
        }
        if (val < 0) {
            input.value = 0;
            showValueError('⚠️ Giá trị giảm không được âm');
            return false;
        }
    }
    return true;
}

document.addEventListener('DOMContentLoaded', function() {
    var typeSelect = document.getElementById('couponType');
    var valueInput = document.getElementById('couponValue');

    if (typeSelect) {
        typeSelect.addEventListener('change', updateValueMax);
        updateValueMax();
    }

    if (valueInput) {
        ['input', 'change', 'keyup'].forEach(function(evt) {
            valueInput.addEventListener(evt, function() {
                var type = document.getElementById('couponType');
                if (type && type.value === 'percent') {
                    if (parseFloat(this.value) > 100) {
                        this.value = 100;
                        showValueError('⚠️ Giá trị giảm không được vượt quá 100%');
                    } else if (parseFloat(this.value) < 0) {
                        this.value = 0;
                        showValueError('⚠️ Giá trị giảm không được âm');
                    } else {
                        hideValueError();
                    }
                }
            });
        });
    }
});

// ── FORMAT VND
function fmtVND(n) { return new Intl.NumberFormat('vi-VN').format(n) + '₫'; }

// ── DASHBOARD CHARTS (Chart.js)
function initRevenueChart(labels, data) {
    const ctx = document.getElementById('revenueChart');
    if (!ctx) return;
    new Chart(ctx, {
        type: 'line',
        data: {
            labels,
            datasets: [{
                label: 'Doanh thu',
                data,
                borderColor: '#4f86f7',
                backgroundColor: 'rgba(79,134,247,.08)',
                borderWidth: 2.5,
                pointBackgroundColor: '#4f86f7',
                pointRadius: 4,
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true, maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: { ticks: { callback: v => fmtVND(v), font: { size: 11 } },
                     grid: { color: 'rgba(0,0,0,.04)' } },
                x: { ticks: { font: { size: 11 } }, grid: { display: false } }
            }
        }
    });
}

function initStatusChart(labels, data) {
    const ctx = document.getElementById('statusChart');
    if (!ctx) return;
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels,
            datasets: [{
                data,
                backgroundColor: ['#ff9f43','#00cfe8','#4f86f7','#7367f0','#28c76f','#ea5455'],
                borderWidth: 0,
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true, maintainAspectRatio: false,
            plugins: { legend: { position: 'bottom', labels: { font: { size: 12 }, padding: 12 } } },
            cutout: '65%'
        }
    });
}