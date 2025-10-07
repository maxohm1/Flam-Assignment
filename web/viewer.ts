// TypeScript Web Viewer for CV Processing Results
// This viewer displays processed camera frames and statistics from the Android app

interface FrameData {
    image: string;           // Base64 encoded image
    fps: number;            // Frames per second
    resolution: string;     // Image resolution (e.g., "1920x1080")
    processingMode: string; // "RAW", "GRAYSCALE", "EDGE_DETECTION"
    timestamp: number;      // Unix timestamp
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
    
    private currentMode: string = "GRAYSCALE";
    private modes: string[] = ["RAW", "GRAYSCALE", "EDGE_DETECTION"];
    
    constructor() {
        this.initializeElements();
        this.setupEventListeners();
        this.loadSampleData();
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
    
    private refreshData(): void {
        console.log('Refreshing data...');
        // In a real implementation, this would fetch data from the Android app
        // For now, generate new sample data
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
    
    private clearData(): void {
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
