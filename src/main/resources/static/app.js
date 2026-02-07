const profileForm = document.getElementById('profileForm');
const postForm = document.getElementById('postForm');
const searchForm = document.getElementById('searchForm');
const profilesContainer = document.getElementById('profiles');
const postsContainer = document.getElementById('posts');
const searchResults = document.getElementById('searchResults');
const statusBox = document.getElementById('status');

function setStatus(message, isError = false) {
    statusBox.textContent = message;
    statusBox.style.background = isError ? '#9f1239' : '#1f2937';
}

function renderList(container, items, templateFn) {
    container.innerHTML = '';
    if (!items.length) {
        container.innerHTML = '<div class="small">No data.</div>';
        return;
    }
    items.forEach(item => {
        const div = document.createElement('div');
        div.className = 'item';
        div.innerHTML = templateFn(item);
        container.appendChild(div);
    });
}

async function api(path, options = {}) {
    const response = await fetch(path, {
        headers: { 'Content-Type': 'application/json' },
        ...options
    });
    if (!response.ok) {
        const error = await response.json().catch(() => ({ message: `HTTP ${response.status}` }));
        throw new Error(error.message || `HTTP ${response.status}`);
    }
    if (response.status === 204) return null;
    return response.json();
}

async function loadProfiles() {
    const profiles = await api('/api/profiles');
    renderList(profilesContainer, profiles, p => `
      <strong>${p.username}</strong>
      <div>${p.bio || ''}</div>
      <div class="small">id=${p.id}, interests: ${p.interests || '-'}</div>
    `);
}

async function loadPosts() {
    const posts = await api('/api/posts/sorted/newest');
    renderList(postsContainer, posts, p => `
      <strong>Post #${p.id}</strong>
      <div>${p.content}</div>
      <div class="small">profileId=${p.profileId}, createdAt=${p.createdAt}</div>
    `);
}

profileForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        await api('/api/profiles', {
            method: 'POST',
            body: JSON.stringify({
                username: document.getElementById('username').value,
                bio: document.getElementById('bio').value,
                interests: document.getElementById('interests').value
            })
        });
        profileForm.reset();
        await loadProfiles();
        setStatus('Profile created.');
    } catch (err) {
        setStatus(`Create profile failed: ${err.message}`, true);
    }
});

postForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        await api('/api/posts', {
            method: 'POST',
            body: JSON.stringify({
                profileId: Number(document.getElementById('profileId').value),
                content: document.getElementById('content').value
            })
        });
        postForm.reset();
        await loadPosts();
        setStatus('Post created.');
    } catch (err) {
        setStatus(`Create post failed: ${err.message}`, true);
    }
});

searchForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        const query = encodeURIComponent(document.getElementById('searchUsername').value || '');
        const results = await api(`/api/profiles/search?username=${query}`);
        renderList(searchResults, results, p => `<strong>${p.username}</strong><div class="small">id=${p.id}</div>`);
        setStatus(`Found ${results.length} profile(s).`);
    } catch (err) {
        setStatus(`Search failed: ${err.message}`, true);
    }
});

document.getElementById('reloadProfiles').addEventListener('click', () => loadProfiles().catch(e => setStatus(e.message, true)));
document.getElementById('reloadPosts').addEventListener('click', () => loadPosts().catch(e => setStatus(e.message, true)));

(async function init() {
    try {
        await loadProfiles();
        await loadPosts();
        setStatus('Frontend ready.');
    } catch (err) {
        setStatus(`Initialization failed: ${err.message}`, true);
    }
})();
