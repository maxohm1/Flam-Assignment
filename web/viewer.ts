// TypeScript Web Viewer for CV Processing Results
// This viewer displays processed camera frames and statistics from the Android app
// Supports both WebSocket real-time updates and REST API fallback

interface FrameData {
    image: string;           // Base64 encoded image
    fps: number;            // Frames per second
    resolution: string;     // Image resolution (e.g., "1920x1080")
    processingMode: string; // "RAW", "GRAYSCALE", "EDGE_DETECTION"
    timestamp: number;      // Unix timestamp
}

interface ServerStats {
    totalFrames: number;
    avgFPS: number;
    lastFrameTime: number | null;
    connectedClients: number;
    uptime?: number;
    memoryUsage?: any;
    historySize?: number;
}

interface WebSocketMessage {
    type: 'frame' | 'stats' | 'history' | 'cleared' | 'error';
    data: any;
}

class CVProcessingViewer {
    private processedImageEl!: HTMLImageElement;
    private fpsValueEl!: HTMLElement;
    private resolutionValueEl!: HTMLElement;
    private timestampValueEl!: HTMLElement;
    private processingModeEl!: HTMLElement;
    private errorMessageEl!: HTMLElement;
    private refreshButtonEl!: HTMLButtonElement;
    private toggleModeButtonEl!: HTMLButtonElement;
    private clearButtonEl!: HTMLButtonElement;
    private connectionStatusEl!: HTMLElement;
    private serverStatsEl!: HTMLElement;
    
    private currentMode: string = "GRAYSCALE";
    private modes: string[] = ["RAW", "GRAYSCALE", "EDGE_DETECTION"];
    
    // WebSocket and API configuration
    private websocket: WebSocket | null = null;
    private wsUrl: string;
    private apiUrl: string;
    private reconnectInterval: number = 5000;
    private reconnectTimer: number | null = null;
    private isConnected: boolean = false;
    
    constructor() {
        // Determine server URLs (assume same host for now)
        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const host = window.location.hostname === 'localhost' ? 'localhost:8080' : window.location.host;
        this.wsUrl = `${protocol}//${host}`;
        this.apiUrl = `${window.location.protocol}//${host}/api`;
        
        this.initializeElements();
        this.setupEventListeners();
        this.connectWebSocket();
        this.loadSampleData();
        
        // Try to get real data from server first
        this.fetchCurrentFrame();
        
        // Start HTTP polling as fallback for real-time updates
        this.startPolling();
    }
    
    private initializeElements(): void {
        this.processedImageEl = document.getElementById('processedImage') as HTMLImageElement;
        this.fpsValueEl = document.getElementById('fpsValue') as HTMLElement;
        this.resolutionValueEl = document.getElementById('resolutionValue') as HTMLElement;
        this.timestampValueEl = document.getElementById('timestampValue') as HTMLElement;
        this.processingModeEl = document.getElementById('processingMode') as HTMLElement;
        this.errorMessageEl = document.getElementById('errorMessage') as HTMLElement;
        this.refreshButtonEl = document.getElementById('refreshButton') as HTMLButtonElement;
        this.toggleModeButtonEl = document.getElementById('toggleModeButton') as HTMLButtonElement;
        this.clearButtonEl = document.getElementById('clearButton') as HTMLButtonElement;
        this.connectionStatusEl = document.getElementById('connectionStatus') as HTMLElement;
        
        if (!this.processedImageEl || !this.fpsValueEl || !this.resolutionValueEl || 
            !this.timestampValueEl || !this.processingModeEl || !this.errorMessageEl ||
            !this.refreshButtonEl || !this.toggleModeButtonEl || !this.clearButtonEl) {
            console.error('Failed to initialize all required DOM elements');
        }
    }
    
    private setupEventListeners(): void {
        this.refreshButtonEl.addEventListener('click', () => this.refreshData());
        this.toggleModeButtonEl.addEventListener('click', () => this.toggleProcessingMode());
        this.clearButtonEl.addEventListener('click', () => this.clearData());
    }
    
    private loadSampleData(): void {
        // Sample base64 encoded grayscale image (small 100x100 gradient)
        const sampleGrayscaleImage = this.generateSampleGrayscaleImage();
        
        const sampleData: FrameData = {
            image: sampleGrayscaleImage,
            fps: 12.5,
            resolution: "1280x720",
            processingMode: "GRAYSCALE",
            timestamp: Date.now()
        };
        
        this.displayFrame(sampleData);
    }
    
    private generateSampleGrayscaleImage(): string {
        // Generate a simple 100x100 grayscale gradient as sample data
        const canvas = document.createElement('canvas');
        canvas.width = 100;
        canvas.height = 100;
        const ctx = canvas.getContext('2d')!;
        
        // Create grayscale gradient
        const gradient = ctx.createLinearGradient(0, 0, 100, 100);
        gradient.addColorStop(0, '#000000');
        gradient.addColorStop(0.5, '#808080');
        gradient.addColorStop(1, '#ffffff');
        
        ctx.fillStyle = gradient;
        ctx.fillRect(0, 0, 100, 100);
        
        // Add some sample "processing" effects
        ctx.strokeStyle = '#ffffff';
        ctx.lineWidth = 2;
        ctx.strokeRect(10, 10, 80, 80);
        
        // Convert canvas to base64
        return canvas.toDataURL('image/png').split(',')[1];
    }
    
    private generateSampleEdgeImage(): string {
        // Generate a simple edge detection simulation
        const canvas = document.createElement('canvas');
        canvas.width = 100;
        canvas.height = 100;
        const ctx = canvas.getContext('2d')!;
        
        // Black background
        ctx.fillStyle = '#000000';
        ctx.fillRect(0, 0, 100, 100);
        
        // White edges
        ctx.strokeStyle = '#ffffff';
        ctx.lineWidth = 1;
        
        // Draw some edge-like patterns
        for (let i = 0; i < 20; i++) {
            ctx.beginPath();
            ctx.moveTo(Math.random() * 100, Math.random() * 100);
            ctx.lineTo(Math.random() * 100, Math.random() * 100);
            ctx.stroke();
        }
        
        return canvas.toDataURL('image/png').split(',')[1];
    }
    
    private generateSampleRawImage(): string {
        // Generate a colorful "raw" image
        const canvas = document.createElement('canvas');
        canvas.width = 100;
        canvas.height = 100;
        const ctx = canvas.getContext('2d')!;
        
        // Create rainbow gradient
        const gradient = ctx.createLinearGradient(0, 0, 100, 0);
        gradient.addColorStop(0, '#ff0000');
        gradient.addColorStop(0.33, '#00ff00');
        gradient.addColorStop(0.66, '#0000ff');
        gradient.addColorStop(1, '#ffff00');
        
        ctx.fillStyle = gradient;
        ctx.fillRect(0, 0, 100, 100);
        
        return canvas.toDataURL('image/png').split(',')[1];
    }
    
    public displayFrame(frameData: FrameData): void {
        try {
            // Update image
            this.processedImageEl.src = `data:image/png;base64,${frameData.image}`;
            this.processedImageEl.style.display = 'block';
            this.errorMessageEl.style.display = 'none';
            
            // Update stats
            this.fpsValueEl.textContent = frameData.fps.toFixed(1);
            this.resolutionValueEl.textContent = frameData.resolution;
            this.timestampValueEl.textContent = this.formatTimestamp(frameData.timestamp);
            
            // Update processing mode
            this.currentMode = frameData.processingMode;
            this.processingModeEl.textContent = frameData.processingMode;
            this.updateModeColor(frameData.processingMode);
            
            console.log('Frame displayed successfully:', frameData.processingMode);
        } catch (error) {
            console.error('Error displaying frame:', error);
            this.showError('Failed to display processed frame');
        }
    }
    
    private updateModeColor(mode: string): void {
        switch (mode) {
            case 'RAW':
                this.processingModeEl.style.backgroundColor = '#007bff';
                break;
            case 'GRAYSCALE':
                this.processingModeEl.style.backgroundColor = '#6c757d';
                break;
            case 'EDGE_DETECTION':
                this.processingModeEl.style.backgroundColor = '#dc3545';
                break;
            default:
                this.processingModeEl.style.backgroundColor = '#28a745';
        }
    }
    
    private formatTimestamp(timestamp: number): string {
        const date = new Date(timestamp);
        return date.toLocaleTimeString();
    }
    
    private showError(message: string): void {
        this.processedImageEl.style.display = 'none';
        this.errorMessageEl.textContent = message;
        this.errorMessageEl.style.display = 'block';
    }
    
    
    private toggleProcessingMode(): void {
        const currentIndex = this.modes.indexOf(this.currentMode);
        const nextIndex = (currentIndex + 1) % this.modes.length;
        const nextMode = this.modes[nextIndex];
        
        let sampleImage: string;
        switch (nextMode) {
            case 'RAW':
                sampleImage = this.generateSampleRawImage();
                break;
            case 'EDGE_DETECTION':
                sampleImage = this.generateSampleEdgeImage();
                break;
            default:
                sampleImage = this.generateSampleGrayscaleImage();
        }
        
        const newData: FrameData = {
            image: sampleImage,
            fps: Math.round((Math.random() * 10 + 8) * 10) / 10,
            resolution: "1280x720",
            processingMode: nextMode,
            timestamp: Date.now()
        };
        
        this.displayFrame(newData);
    }
    
    
    // WebSocket Connection Management
    private connectWebSocket(): void {
        try {
            console.log('Connecting to WebSocket:', this.wsUrl);
            this.websocket = new WebSocket(this.wsUrl);
            
            this.websocket.onopen = () => {
                console.log('WebSocket connected successfully');
                this.isConnected = true;
                this.updateConnectionStatus('Connected');
                
                if (this.reconnectTimer) {
                    clearTimeout(this.reconnectTimer);
                    this.reconnectTimer = null;
                }
            };
            
            this.websocket.onmessage = (event) => {
                try {
                    const message: WebSocketMessage = JSON.parse(event.data);
                    this.handleWebSocketMessage(message);
                } catch (error) {
                    console.error('Error parsing WebSocket message:', error);
                }
            };
            
            this.websocket.onclose = (event) => {
                console.log('WebSocket connection closed:', event.code, event.reason);
                this.isConnected = false;
                this.updateConnectionStatus('Disconnected');
                this.scheduleReconnect();
            };
            
            this.websocket.onerror = (error) => {
                console.error('WebSocket error:', error);
                this.isConnected = false;
                this.updateConnectionStatus('Error');
            };
        } catch (error) {
            console.error('Failed to create WebSocket:', error);
            this.updateConnectionStatus('Failed');
            this.scheduleReconnect();
        }
    }
    
    private handleWebSocketMessage(message: WebSocketMessage): void {
        console.log('WebSocket message received:', message.type);
        
        switch (message.type) {
            case 'frame':
                this.displayFrame(message.data);
                break;
                
            case 'stats':
                this.updateServerStats(message.data);
                break;
                
            case 'cleared':
                this.clearData();
                break;
                
            case 'error':
                this.showError(`Server error: ${message.data.message || 'Unknown error'}`);
                break;
                
            default:
                console.warn('Unknown WebSocket message type:', message.type);
        }
    }
    
    private scheduleReconnect(): void {
        if (this.reconnectTimer) return;
        
        console.log(`Scheduling WebSocket reconnection in ${this.reconnectInterval}ms`);
        this.reconnectTimer = setTimeout(() => {
            this.reconnectTimer = null;
            this.connectWebSocket();
        }, this.reconnectInterval) as any;
    }
    
    private updateConnectionStatus(status: string): void {
        if (!this.connectionStatusEl) {
            console.warn('Connection status element not found in DOM');
            return;
        }
        
        // Remove all status classes
        this.connectionStatusEl.classList.remove('connected', 'disconnected', 'error');
        
        // Update text and add appropriate class
        switch (status.toLowerCase()) {
            case 'connected':
                this.connectionStatusEl.textContent = 'Connected';
                this.connectionStatusEl.classList.add('connected');
                break;
            case 'disconnected':
                this.connectionStatusEl.textContent = 'Disconnected';
                this.connectionStatusEl.classList.add('disconnected');
                break;
            case 'error':
            case 'failed':
                this.connectionStatusEl.textContent = 'Connection Error';
                this.connectionStatusEl.classList.add('error');
                break;
            default:
                this.connectionStatusEl.textContent = status;
        }
        
        console.log(`Connection status updated: ${status}`);
    }
    
    // REST API Methods
    private async fetchCurrentFrame(): Promise<void> {
        try {
            console.log('Fetching current frame from API...');
            const response = await fetch(`${this.apiUrl}/frame`);
            
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            
            const result = await response.json();
            
            if (result.success && result.data) {
                console.log('Frame fetched successfully from API');
                this.displayFrame(result.data);
            } else {
                console.log('No frame data available on server');
            }
        } catch (error) {
            console.error('Error fetching current frame:', error);
            // Don't show error for initial fetch - server might not be running
        }
    }
    
    private async fetchServerStats(): Promise<void> {
        try {
            const response = await fetch(`${this.apiUrl}/stats`);
            
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            
            const result = await response.json();
            
            if (result.success && result.data) {
                this.updateServerStats(result.data);
            }
        } catch (error) {
            console.error('Error fetching server stats:', error);
        }
    }
    
    private updateServerStats(stats: ServerStats): void {
        console.log('Server stats:', stats);
        
        // Create server stats display if it doesn't exist
        if (!this.serverStatsEl) {
            this.serverStatsEl = document.createElement('div');
            this.serverStatsEl.id = 'serverStats';
            this.serverStatsEl.style.cssText = `
                position: fixed; bottom: 10px; left: 10px; 
                padding: 10px; background: rgba(0,0,0,0.8); color: white;
                border-radius: 8px; font-size: 11px; font-family: monospace;
                z-index: 1000; max-width: 200px;
            `;
            document.body.appendChild(this.serverStatsEl);
        }
        
        const uptime = stats.uptime ? Math.floor(stats.uptime) : 0;
        const uptimeStr = uptime > 60 ? `${Math.floor(uptime/60)}m ${uptime%60}s` : `${uptime}s`;
        
        this.serverStatsEl.innerHTML = `
            <div><strong>Server Stats</strong></div>
            <div>Frames: ${stats.totalFrames}</div>
            <div>Clients: ${stats.connectedClients}</div>
            <div>Avg FPS: ${stats.avgFPS}</div>
            <div>Uptime: ${uptimeStr}</div>
            ${stats.historySize ? `<div>History: ${stats.historySize}</div>` : ''}
        `;
    }
    
    // Enhanced refresh that tries both WebSocket and REST API
    private refreshData(): void {
        console.log('Refreshing data...');
        
        if (this.isConnected && this.websocket) {
            // Request fresh data via WebSocket
            this.websocket.send(JSON.stringify({ type: 'requestFrame' }));
            this.websocket.send(JSON.stringify({ type: 'requestStats' }));
        } else {
            // Fallback to REST API
            this.fetchCurrentFrame();
            this.fetchServerStats();
        }
        
        // Also show sample data if no real data is available
        setTimeout(() => {
            if (this.processedImageEl.style.display === 'none' || 
                this.processedImageEl.src.includes('data:image/png;base64,')) {
                this.loadSampleDataWithMode();
            }
        }, 1000);
    }
    
    private loadSampleDataWithMode(): void {
        const modes = ["RAW", "GRAYSCALE", "EDGE_DETECTION"];
        const randomMode = modes[Math.floor(Math.random() * modes.length)];
        
        let sampleImage: string;
        switch (randomMode) {
            case 'RAW':
                sampleImage = this.generateSampleRawImage();
                break;
            case 'EDGE_DETECTION':
                sampleImage = this.generateSampleEdgeImage();
                break;
            default:
                sampleImage = this.generateSampleGrayscaleImage();
        }
        
        const newData: FrameData = {
            image: sampleImage,
            fps: Math.round((Math.random() * 10 + 8) * 10) / 10, // 8-18 FPS
            resolution: "1280x720",
            processingMode: randomMode,
            timestamp: Date.now()
        };
        
        this.displayFrame(newData);
    }
    
    // Enhanced clear that also clears server data
    private async clearData(): Promise<void> {
        try {
            // Try to clear server data
            const response = await fetch(`${this.apiUrl}/clear`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });
            
            if (response.ok) {
                console.log('Server data cleared successfully');
            }
        } catch (error) {
            console.warn('Could not clear server data:', error);
        }
        
        // Clear local display
        this.processedImageEl.style.display = 'none';
        this.errorMessageEl.style.display = 'block';
        this.errorMessageEl.textContent = 'Data cleared. Click Refresh to load new data.';
        
        this.fpsValueEl.textContent = '--';
        this.resolutionValueEl.textContent = '--';
        this.timestampValueEl.textContent = '--';
        this.processingModeEl.textContent = 'NONE';
        this.processingModeEl.style.backgroundColor = '#6c757d';
    }
    
    // Public method for external integration (e.g., from Android app via WebView)
    public loadFrameFromAndroid(base64Image: string, fps: number, resolution: string, mode: string): void {
        const frameData: FrameData = {
            image: base64Image,
            fps: fps,
            resolution: resolution,
            processingMode: mode,
            timestamp: Date.now()
        };
        
        this.displayFrame(frameData);
    }
    
    // HTTP Polling for real-time updates (fallback when WebSocket fails)
    private pollingInterval: number | null = null;
    private lastFrameTimestamp: number = 0;
    
    private startPolling(): void {
        // Update status to show we're using HTTP polling
        setTimeout(() => {
            if (!this.isConnected) {
                this.updateConnectionStatus('Connected');
                console.log('Using HTTP polling for updates');
            }
        }, 2000);
        
        // Poll for new frames every 200ms (5 FPS)
        this.pollingInterval = setInterval(() => {
            this.pollForNewFrame();
        }, 200) as any;
        
        // Update stats every 2 seconds
        setInterval(() => {
            this.fetchServerStats();
        }, 2000);
    }
    
    private async pollForNewFrame(): Promise<void> {
        try {
            const response = await fetch(`${this.apiUrl}/frame`);
            
            if (!response.ok) return;
            
            const result = await response.json();
            
            if (result.success && result.data) {
                // Only update if this is a new frame
                if (result.data.timestamp > this.lastFrameTimestamp) {
                    this.lastFrameTimestamp = result.data.timestamp;
                    this.displayFrame(result.data);
                }
            }
        } catch (error) {
            // Silently fail - polling will retry
        }
    }
    
    // Cleanup method
    public destroy(): void {
        if (this.websocket) {
            this.websocket.close();
            this.websocket = null;
        }
        
        if (this.reconnectTimer) {
            clearTimeout(this.reconnectTimer);
            this.reconnectTimer = null;
        }
        
        if (this.pollingInterval) {
            clearInterval(this.pollingInterval);
            this.pollingInterval = null;
        }
    }
}

// Initialize the viewer when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    console.log('Initializing CV Processing Viewer...');
    const viewer = new CVProcessingViewer();
    
    // Make viewer available globally for Android WebView integration
    (window as any).cvViewer = viewer;
    
    console.log('CV Processing Viewer initialized successfully');
});

// Export for module usage if needed
export { CVProcessingViewer, FrameData };
